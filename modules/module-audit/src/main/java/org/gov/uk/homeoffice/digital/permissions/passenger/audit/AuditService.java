package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import com.opencsv.CSVWriter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.io.output.StringBuilderWriter;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
public class AuditService {

    public static final Authentication AUTHENTICATION = SecurityContextHolder.getContext().getAuthentication();

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private static final String ERROR = "AuditError";

    private static final String[] HEADER = {
            "Event ID",
            "UserName",
            "Action",
            "Outcome",
            "Date and time"
    };

    private static final DateTimeFormatter FORMATTER = ofPattern("ddMMyyyy");
    public static final String NO_DATA_IS_AVAILABLE_FOR_THIS_PERIOD = "No data is available for this period.";

    private final Jdbi dbi;
    private final Counter counter;

    @Autowired
    public AuditService(final Jdbi dbi, final MeterRegistry meterRegistry) {
        this.dbi = dbi;
        this.counter = meterRegistry.counter("audit", "error", "count");
    }

    public void audit(final String action, final String result) {
        this.audit(action, result, ((OidcUser) AUTHENTICATION.getPrincipal()).getUserInfo().getPreferredUsername()
        );
    }

    public void audit(final String action, final String result, final String user) {
        audit(new Audit(null, user, now(), result, action));
    }

    public void audit(Audit audit) {
        try {
            final AuditDAO dao = dbi.onDemand(AuditDAO.class);
            dao.insert(audit);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            counter.count();
        }
    }

    public String exportAuditLog(int month, int year) throws IOException {
        if (month == 0 || year == 0) {
            throw new IllegalArgumentException(format("Invalid values of Month: %s and/or Year %s", month, year));
        }

        LocalDate inputDate = LocalDate.of(year, month, 1);

        String toDate = FORMATTER.format(getDateWithLastDayOfSuppliedMonth(month, year, inputDate));
        String fromDate = FORMATTER.format(getDateWithLastDayOfSuppliedMonth(month, year, inputDate).minusMonths(1));

        StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
        CSVWriter csvWriter = getCsvWriter(stringBuilderWriter);

        getCsv(toDate, fromDate, csvWriter, null, null);

        return stringBuilderWriter.toString();
    }


    public String exportAuditLog(LocalDate fromDate, LocalDate toDate, String userName, String teamName) throws IOException {

        StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
        CSVWriter csvWriter = getCsvWriter(stringBuilderWriter);

        if (fromDate == null
                || toDate == null
                || toDate.isBefore(fromDate)
                || fromDate.plusMonths(7).plusDays(1).isBefore(toDate)
                || fromDate.plusMonths(2).plusDays(1).isBefore(toDate) && isEmpty(userName) && isEmpty(teamName)) {
            generateCsvWithErrorMessage(csvWriter, new String[]{NO_DATA_IS_AVAILABLE_FOR_THIS_PERIOD});
            return stringBuilderWriter.toString();
        }

        String toDateStr = FORMATTER.format(toDate);
        String fromDateStr = FORMATTER.format(fromDate);
        getCsv(toDateStr, fromDateStr, csvWriter, userName, teamName);

        return stringBuilderWriter.toString();
    }

    private void getCsv(String toDateStr, String fromDateStr, CSVWriter csvWriter, String userName, String teamName) throws IOException {
        if (!isEmpty(userName) && !isEmpty(teamName)) {
            getCsvForUserAndTeam(csvWriter, fromDateStr, toDateStr, userName, teamName);
        } else if (!isEmpty(userName)) {
            getCsvForUser(csvWriter, fromDateStr, toDateStr, userName);
        } else if (!isEmpty(teamName)) {
            getCsvForTeam(csvWriter, fromDateStr, toDateStr, teamName);
        } else {
            getCsv(csvWriter, fromDateStr, toDateStr);
        }
    }

    private void getCsv(CSVWriter csvWriter, String fromDate, String toDate) throws IOException {
        try {
            final AuditDAO dao = dbi.onDemand(AuditDAO.class);
            final Collection<Audit> audits = dao.selectInRange(fromDate, toDate);
            generateCsv(csvWriter, audits);
        } finally {
            csvWriter.close();
        }
    }

    private void getCsvForUser(CSVWriter csvWriter, String fromDate, String toDate, String userName) throws IOException {
        try {
            final AuditDAO dao = dbi.onDemand(AuditDAO.class);
            final Collection<Audit> audits = dao.selectInRangeForUser(fromDate, toDate, userName);
            generateCsv(csvWriter, audits);
        } finally {
            csvWriter.close();
        }
    }

    private void getCsvForTeam(CSVWriter csvWriter, String fromDate, String toDate, String teamName) throws IOException {
        try {
            final AuditDAO dao = dbi.onDemand(AuditDAO.class);
            final Collection<Audit> audits = dao.selectInRangeForTeam(fromDate, toDate, getValueForLikeQuery(teamName));
            generateCsv(csvWriter, audits);
        } finally {
            csvWriter.close();
        }
    }

    private String getValueForLikeQuery(String teamName) {
        return "%" + teamName + "%";
    }

    private void getCsvForUserAndTeam(CSVWriter csvWriter, String fromDate, String toDate, String userName, String teamName) throws IOException {
        try {
            final AuditDAO dao = dbi.onDemand(AuditDAO.class);
            final Collection<Audit> audits = dao.selectInRangeForUserAndTeam(fromDate, toDate, userName, getValueForLikeQuery(teamName));
            generateCsv(csvWriter, audits);
        } finally {
            csvWriter.close();
        }
    }

    private void generateCsv(CSVWriter csvWriter, Collection<Audit> audits) {
        if (audits == null || audits.isEmpty()) {
            generateCsvWithErrorMessage(csvWriter, new String[]{NO_DATA_IS_AVAILABLE_FOR_THIS_PERIOD});
        } else {
            audits.stream().map(Audit::toAuditArray).forEach(csvWriter::writeNext);
        }
    }

    private void generateCsvWithErrorMessage(CSVWriter csvWriter, String[] message) {
        ArrayList<String[]> allLines = new ArrayList<>();
        allLines.add(message);
        csvWriter.writeAll(allLines);
    }

    private LocalDate getDateWithLastDayOfSuppliedMonth(int month, int year, LocalDate inputDate) {
        return LocalDate.of(year, month, inputDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()).plusDays(1);
    }

    private CSVWriter getCsvWriter(final Writer writer) {
        final CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeNext(HEADER);
        return csvWriter;
    }
}
