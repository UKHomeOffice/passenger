package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model.AuditSearchForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;

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

    @PreAuthorize("hasRole('AUDIT')")
    @GetMapping
    public ModelAndView GETaudit(@SessionAttribute(value="auditSearchForm", required = false) AuditSearchForm auditSearchForm,
                                 @RequestParam(name = "new", required = false) String newSearch,
                                 @RequestParam(name = "page", required = false) String pageNumber) {
        LOGGER.debug("Showing audit search view");

        if (newSearch != null && newSearch.equals("true")) {
            getSession().removeAttribute("auditSearchForm");
            auditSearchForm = new AuditSearchForm();
        }

        if (auditSearchForm != null) {
            if (pageNumber == null) {
                pageNumber = "1";
            }
            switch (pageNumber) {
                case "next":
                    auditSearchForm.incrementPageNumber();
                    break;
                case "prev":
                    auditSearchForm.decrementPageNumber();
                    break;
                default:
                    auditSearchForm.setCurrentPageNumber(Integer.valueOf(pageNumber));
            }
        }

        return new ModelAndView("audit/audit-search", "auditSearchForm",
                auditSearchForm == null ? new AuditSearchForm() : auditSearchForm);
    }

    @PreAuthorize("hasRole('AUDIT')")
    @PostMapping
    public ModelAndView POSTaudit(@ModelAttribute(value="auditSearchForm") final AuditSearchForm auditSearchForm) {
        LOGGER.debug("Calling search for email {}, passport number {} and name {}",
                auditSearchForm.getEmailAddress(), auditSearchForm.getPassportNumber(), auditSearchForm.getName());

        // Check whether form is empty.
        if (auditSearchForm.isEmpty()) {
            auditSearchForm.setAuditEntries(Collections.emptyList());
            return new ModelAndView("audit/audit-search", "auditSearchForm", auditSearchForm);
        }

        // Get the results
        final List<Audit> audits = search(auditSearchForm.getEmailAddress(),
                auditSearchForm.getPassportNumber(),
                auditSearchForm.getName(),
                auditSearchForm.getAdministratorOnlyEmail(),
                auditSearchForm.getFrom(),
                auditSearchForm.getTo());

        // Update
        auditSearchForm.setAuditEntries(audits);

        // Store in session to allow pagination
        getSession().setAttribute("auditSearchForm", auditSearchForm);

        return new ModelAndView("audit/audit-search", "auditSearchForm", auditSearchForm);
    }

    private List<Audit> search(final String emailAddress,
                                     final String passportNumber,
                                     final String name,
                                     final Boolean adminOnly,
                                     final LocalDate from,
                                     final LocalDate to) {
        if (adminOnly) {
            return newArrayList(adminAuditSearch.findByQuery(emailAddress, null,
                    passportNumber, name, from, to));
        }
        else {
            return Stream.concat(
                    adminAuditSearch.findByQuery(null, emailAddress, passportNumber, name, from, to).stream(),
                    publicAuditSearch.findByQuery(null, emailAddress, passportNumber, name, from, to).stream())
                .sorted(Comparator.comparing(Audit::getDateTime).reversed())
                .collect(Collectors.toList());
        }
    }

    private HttpSession getSession() {
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

}
