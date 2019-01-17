package org.gov.uk.homeoffice.digital.permissions.passenger.issue.ui.controller;

import com.google.common.base.Strings;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.gov.uk.homeoffice.digital.permissions.passenger.issue.ui.model.TechnicalIssueForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.service.notify.SendEmailResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class TechnicalIssueFormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalIssueFormController.class);

    private final NotifyService notifyService;

    @Autowired
    public TechnicalIssueFormController(final NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @GetMapping(path = "/footer/issue")
    public ModelAndView GETtechnicalIssueForm() {
        return new ModelAndView("technical-issue", "issueForm", new TechnicalIssueForm());
    }

    @PostMapping(path = "/footer/issue")
    public ModelAndView POSTtechnicalIssueForm(@ModelAttribute(value="issueForm") final TechnicalIssueForm technicalIssueForm) {

        // Validate form is not empty
        if (Strings.isNullOrEmpty(technicalIssueForm.getEmailAddress()) ||
                Strings.isNullOrEmpty(technicalIssueForm.getName()) ||
                Strings.isNullOrEmpty(technicalIssueForm.getPassportNumber()) ||
                Strings.isNullOrEmpty(technicalIssueForm.getIssueDetail())) {
            final Map<String, Object> model = new HashMap<>();
            model.put("issueForm", technicalIssueForm);
            model.put("error", Boolean.TRUE);
            return new ModelAndView("technical-issue", model);
        }

        LOGGER.debug("Issue raised from {}, passport number {}", technicalIssueForm.getEmailAddress(),
                technicalIssueForm.getPassportNumber());

        // Send the issue via email
        final Optional<SendEmailResponse> response = notifyService.sendTechnicalIssueEmail(
                technicalIssueForm.getEmailAddress(),
                technicalIssueForm.getName(),
                technicalIssueForm.getPassportNumber(),
                technicalIssueForm.getIssueDetail());

        if (!response.isPresent())
            LOGGER.warn("Technical issue form was not sent.");

        return new ModelAndView("technical-issue-confirm");
    }

}
