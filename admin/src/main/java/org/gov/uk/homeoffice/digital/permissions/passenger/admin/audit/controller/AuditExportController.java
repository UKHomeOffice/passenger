package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.controller;

import com.opencsv.CSVWriter;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model.AuditDateRangeForm;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/audit-export")
public class AuditExportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    private static final String[] EXPORT_HEADERS = new String[] { "Username", "Date/Time", "Result", "Content",
            "Passenger Name", "Passenger Email", "Passenger Passport"};

    private final AuditService adminAuditSearch;
    private final AuditService publicAuditSearch;

    @Autowired
    public AuditExportController(@Qualifier("audit.admin") final AuditService adminAuditSearch,
                                 @Qualifier("audit.public") final AuditService publicAuditSearch) {
        this.adminAuditSearch = adminAuditSearch;
        this.publicAuditSearch = publicAuditSearch;
    }

    @PreAuthorize("hasRole('AUDIT')")
    @GetMapping
    public ModelAndView GETauditExport() {
        LOGGER.debug("Audit export");
        return new ModelAndView("audit/audit-export", "auditDateRangeForm",
                new AuditDateRangeForm());
    }

    @PreAuthorize("hasRole('AUDIT')")
    @PostMapping
    public void POSTauditExport(
            @ModelAttribute(value="auditDateRangeForm") final AuditDateRangeForm dateRangeForm,
            final HttpServletResponse response) throws IOException {

        final LocalDate fromDate = dateRangeForm.getFrom();
        final LocalDate toDate = dateRangeForm.getTo();

        LOGGER.debug("Exporting audit in range {} to {}", fromDate.toString(), toDate.toString());

        final Collection<Audit> audits = Stream.concat(
                    adminAuditSearch.findByDateRange(fromDate, toDate).stream(),
                    publicAuditSearch.findByDateRange(fromDate, toDate).stream())
                .sorted(Comparator.comparing(Audit::getDateTime))
                .collect(Collectors.toList());

        final String csvFileName = "audit-export.csv";
        final String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

        final CSVWriter csvWriter = new CSVWriter(response.getWriter());
        csvWriter.writeAll(toStringArray(audits));
        csvWriter.close();
    }

    private static List<String[]> toStringArray(Collection<Audit> audits) {
        final List<String[]> records = new ArrayList<>();

        // adding header record
        records.add(EXPORT_HEADERS);

        // Load records into CSV array
        for (final Audit audit : audits) {
            records.add(new String[] {
                    audit.getUserName(),
                    audit.getDateTime().toString(),
                    audit.getResult(),
                    audit.getContent(),
                    audit.getPassengerName(),
                    audit.getPassengerEmail(),
                    audit.getPassengerPassportNumber()
            });
        }

        return records;
    }

}
