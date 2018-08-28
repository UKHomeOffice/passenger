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
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher.convertToRuntime;

@Controller
public class CrsFileUploadController {

    @Autowired
    CrsFileUploadService crsFileUploadService;

    @Autowired
    @Qualifier("audit.admin")
    private AuditService auditService;

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
            try {
                FileUtils.writeByteArrayToFile(tempFile, file.getBytes());
                final CrsParsedResult result = crsFileUploadService.process(tempFile, SecurityUtil.username());
                redirectAttributes.addFlashAttribute("errors", result.getParseErrors());
                redirectAttributes.addFlashAttribute("crsRecords", result.getUpdatedCrsRecords());

                audit(file, SecurityUtil.username(), result);

                return result.getParseErrors().isEmpty() ? "redirect:/crsrecords" : "redirect:/crsrecords#errors";

            } finally {
                FileUtils.forceDelete(tempFile);
            }
        });
    }

    private void audit(MultipartFile file, String username, CrsParsedResult result) {
        final List<CrsRecord> crsRecords = result.getCrsRecords()
                .stream()
                .filter(crsRecord -> crsRecord.getId() != null && crsRecord.getId() > 0)
                .collect(Collectors.toList());

        int size = crsRecords.size();
        long idFrom = 0;
        long idTo = 0;
        if (size > 0) {
            idFrom = crsRecords.get(0).getId();
            idTo = crsRecords.get(size - 1).getId();
        }
        auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', numberOfRecords='%s', idRange=[%s-%s]",
                file.getOriginalFilename(), size, idFrom, idTo),
                size > 0 ? "SUCCESS" : "FAILURE",
                username);

        result.getParseErrors()
                .stream()
                .forEach(parseError -> auditService.audit(
                        String.format("action='upload', entity='CrsRecord', fileName='%s', row='%s', error='%s'",
                                file.getOriginalFilename(),
                                escape(parseError.crsRow),
                                escape(parseError.message.stream().collect(Collectors.joining(",")))),
                        "FAILURE", username));

        String revokedIds = result.getCrsRecords()
                .stream()
                .filter(crsRecord -> crsRecord.getStatus() == VisaStatus.REVOKED)
                .map(crsRecord -> String.valueOf(crsRecord.getId()))
                .collect(Collectors.joining(","));

        if (!StringUtils.isEmpty(revokedIds)) {
            auditService.audit(String.format("action='upload', entity='CrsRecord', fileName='%s', revokedIds=[%s]",
                    file.getOriginalFilename(), revokedIds), "SUCCESS", username);
        }
    }

    private String escape(String s) {
        return s.replaceAll("'", "\\\\'");
    }

}
