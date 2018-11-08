package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs.CrsFileUploadController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ParticipantsUploadController {

    private final String visaDataSource;
    private final Boolean emailEnabled;
    private final CrsFileUploadController crsFileUploadController;

    public ParticipantsUploadController(@Value("${visa.datasource}") final String visaDataSource,
                                        @Value("${email.enabled}") final Boolean emailEnabled,
                                        final CrsFileUploadController crsFileUploadController) {
        this.visaDataSource = (visaDataSource == null) ? "CRS" : visaDataSource;
        this.emailEnabled = emailEnabled;
        this.crsFileUploadController = crsFileUploadController;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/uploadParticipants"}, method = GET)
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
        return "";
    }

}
