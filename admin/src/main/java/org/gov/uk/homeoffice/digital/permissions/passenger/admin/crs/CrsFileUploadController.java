package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.SecurityUtil;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher.convertToRuntime;

@Controller
public class CrsFileUploadController {

    @Autowired
    private CrsFileUploadService crsFileUploadService;

    @Autowired
    private CrsAuditService crsAuditService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/uploadCrsFile"}, method = RequestMethod.GET)
    public String uploadCrsRecordsView() {
        return "uploadCrsFile";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/uploadCrsFile", method = RequestMethod.POST)
    public String uploadCrsRecords(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Authentication authentication) {
        return convertToRuntime(() -> {
            final File tempFile = Files.createTempFile("part", "csv").toFile();
            return file != null && file.getSize() > 0 ? processFileUpload(file, redirectAttributes, tempFile) : redirectToError(file, "Empty file uploaded", redirectAttributes);
        });
    }

    private String redirectToError(MultipartFile file, String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("fileUploadError", message);
        crsAuditService.audit(SecurityUtil.username(), "FAILURE", "Empty file uploaded");
        return "redirect:/crsrecords#fileUploadError";
    }

    private String processFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, File tempFile) throws IOException {
        try {

            final String fileName = file.getOriginalFilename();

            // Validate file name
            if (fileName == null || !fileName.matches("\\w{5}\\d{2}\\d{2}\\d{4}.csv")) {
                redirectAttributes.addFlashAttribute("errors", List.of(
                        new CrsParseErrors("Please check the data", List.of("File name is incorrect format. Should be in form 'xxxxxddmmyyyy.csv'"))));
                redirectAttributes.addFlashAttribute("crsRecords", Collections.emptyList());
                redirectAttributes.addFlashAttribute("crsRecordsSuccessfullyCreated", 0);
                redirectAttributes.addFlashAttribute("crsRecordsSuccessfullyUpdated", 0);
                redirectAttributes.addFlashAttribute("crsRecordsInError", 1);

                return "redirect:/crsrecords#errors";
            }

            FileUtils.writeByteArrayToFile(tempFile, file.getBytes());

            final CrsParsedResult result = crsFileUploadService.process(tempFile, SecurityUtil.username());

            redirectAttributes.addFlashAttribute("errors", result.getParseErrors());
            redirectAttributes.addFlashAttribute("crsRecords", result.getUpdatedCrsRecords());
            redirectAttributes.addFlashAttribute("crsRecordsSuccessfullyCreated", result.getNumberOfSuccessfullyCreatedRecords());
            redirectAttributes.addFlashAttribute("crsRecordsSuccessfullyUpdated", result.getNumberOfSuccessfullyUpdatedRecords());
            redirectAttributes.addFlashAttribute("crsRecordsInError", result.getNumberOfRecordsInError());

            crsAuditService.audit(file, result);

            return result.getParseErrors().isEmpty() ? "redirect:/crsrecords" : "redirect:/crsrecords#errors";

        } finally {
            FileUtils.forceDelete(tempFile);
        }
    }


}
