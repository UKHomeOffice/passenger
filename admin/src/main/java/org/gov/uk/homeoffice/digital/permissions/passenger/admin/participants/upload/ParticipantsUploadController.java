package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.SecurityUtil;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs.CrsFileUploadController;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ParticipantsAdminService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher.convertToRuntime;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ParticipantsUploadController {

    private final ParticipantsAdminService participantsAdminService;
    private final AuditService auditService;
    private final String visaDataSource;
    private final Boolean emailEnabled;
    private final CrsFileUploadController crsFileUploadController;

    public ParticipantsUploadController(ParticipantsAdminService participantsAdminService,
                                        @Qualifier("audit.admin") AuditService auditService,
                                        @Value("${visa.datasource}") final String visaDataSource,
                                        @Value("${email.enabled}") final Boolean emailEnabled,
                                        final CrsFileUploadController crsFileUploadController) {
        this.participantsAdminService = participantsAdminService;
        this.auditService = auditService;
        this.visaDataSource = (visaDataSource == null) ? "CRS" : visaDataSource;
        this.emailEnabled = emailEnabled;
        this.crsFileUploadController = crsFileUploadController;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/", "/uploadParticipants"}, method = GET)
    public ModelAndView uploadParticipantsView() {
        return new ModelAndView("uploadParticipants",
                Map.of("visaDataSource", visaDataSource, "emailEnabled", emailEnabled));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/uploadParticipants", method = POST)
    public String uploadParticipants(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (visaDataSource.equalsIgnoreCase("CRS")) {
            return crsFileUploadController.uploadCrsRecords(file, redirectAttributes, authentication);
        }
        else {
            return convertToRuntime(() -> {
                final File tempFile = Files.createTempFile("part", "csv").toFile();
                try {
                    FileUtils.writeByteArrayToFile(tempFile, file.getBytes());
                    final ParsedResult result = participantsAdminService.process(tempFile, SecurityUtil.username());
                    redirectAttributes.addFlashAttribute("errors", result.parseErrors);
                    redirectAttributes.addFlashAttribute("uploaded", result.participants);
                    audit(file, SecurityUtil.username(), result);
                    return "redirect:/participants" + (result.parseErrors.isEmpty() ? "" : "#errors");

                } finally {
                    FileUtils.forceDelete(tempFile);
                }
            });
        }
    }

    private void audit(@RequestParam("file") MultipartFile file, String username, ParsedResult result) {
        int size = result.participants.size();
        long idFrom = 0;
        long idTo =  0;
        if(size > 0){
            idFrom = result.participants.get(0).get_1().getId();
            idTo = result.participants.get(size - 1).get_1().getId();
        }
        auditService.audit(String.format("action='upload', entity='Participant', fileName='%s', numberOfRecords='%s', idRange=[%s-%s]",
                file.getOriginalFilename(), size, idFrom, idTo), "SUCCESS", username);

        result.parseErrors
                .stream()
                .forEach(parseError -> auditService.audit(String.format("action='upload', entity='Participant', fileName='%s', row='%s', error='%s'",
                        file.getOriginalFilename(), escape(parseError.csvRow), escape(parseError.exception.toString())), "FAILURE", username
                ));
        String revokedIds = result.participants
                .stream()
                .filter(participantVisaTuple -> participantVisaTuple.get_2().getStatus() == VisaStatus.REVOKED)
                .map(participantVisaTuple -> String.valueOf(participantVisaTuple.get_1().getId()))
                .collect(Collectors.joining(","));

        if (!StringUtils.isEmpty(revokedIds)) {
            auditService.audit(String.format("action='upload', entity='Participant', fileName='%s', revokedIds=[%s]",
                    file.getOriginalFilename(), revokedIds), "SUCCESS", username);
        }



    }

    private String escape(String s) {
        return s.replaceAll("'", "\\\\'");
    }
}
