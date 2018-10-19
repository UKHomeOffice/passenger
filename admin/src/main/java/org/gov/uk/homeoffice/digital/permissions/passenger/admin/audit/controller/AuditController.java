package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model.AuditSearchForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/audit")
public class AuditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    private final AuditService adminAuditSearch;
    private final AuditService publicAuditSearch;

    @Autowired
    public AuditController(@Qualifier("audit.admin") final AuditService adminAuditSearch,
                           @Qualifier("audit.public") final AuditService publicAuditSearch) {
        this.adminAuditSearch = adminAuditSearch;
        this.publicAuditSearch = publicAuditSearch;
    }

    @GetMapping
    public ModelAndView GETaudit() {
        LOGGER.info("Showing audit search view");
        final AuditSearchForm auditSearchForm = new AuditSearchForm();
        return new ModelAndView("audit/audit-search", "auditSearchForm", auditSearchForm);
    }

    @PostMapping
    public ModelAndView POSTaudit(@ModelAttribute(value="auditSearchForm") final AuditSearchForm auditSearchForm) {
        LOGGER.info("Calling search for email {}, passport number {} and name {}",
                auditSearchForm.getEmailAddress(), auditSearchForm.getPassportNumber(), auditSearchForm.getName());

        // Check whether form is empty.
        if (auditSearchForm.isEmpty()) {
            auditSearchForm.setAuditEntries(Collections.emptyList());
            return new ModelAndView("audit/audit-search", "auditSearchForm", auditSearchForm);
        }

        // Get the results
        final Collection<Audit> audits = search(auditSearchForm.getEmailAddress(),
                auditSearchForm.getPassportNumber(),
                auditSearchForm.getName(),
                auditSearchForm.getAdministratorOnlyEmail());

        // Update
        auditSearchForm.setAuditEntries(audits);

        return new ModelAndView("audit/audit-search", "auditSearchForm", auditSearchForm);
    }

    private Collection<Audit> search(final String emailAddress,
                                     final String passportNumber,
                                     final String name,
                                     final Boolean adminOnly) {
        if (adminOnly) {
            return adminAuditSearch.findByQuery(emailAddress, null,
                    passportNumber, name);
        }
        else {
            return Stream.concat(
                    adminAuditSearch.findByQuery(emailAddress, emailAddress, passportNumber, name).stream(),
                    publicAuditSearch.findByQuery(emailAddress, null, passportNumber, name).stream())
                .sorted(Comparator.comparing(Audit::getDateTime))
                .collect(Collectors.toList());
        }
    }

}
