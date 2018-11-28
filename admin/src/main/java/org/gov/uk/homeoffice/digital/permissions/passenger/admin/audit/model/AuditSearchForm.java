package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model;

import com.google.common.base.Strings;
import lombok.Data;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuditSearchForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final double ITEMS_PER_PAGE = 10D;

    private String emailAddress;
    private Boolean administratorOnlyEmail;
    private String passportNumber;
    private String name;
    private List<Audit> auditEntries;
    private int currentPageNumber;
    private int fromPageNumber;
    private int toPageNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

    public AuditSearchForm() {
        this.currentPageNumber = 1;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(emailAddress)
                && Strings.isNullOrEmpty(passportNumber)
                && Strings.isNullOrEmpty(name);
    }

    public List<Audit> getAudits() {
        int fromIndex = ((currentPageNumber - 1) * 10);
        int endIndex = Math.min(fromIndex + (int) ITEMS_PER_PAGE, auditEntries.size());
        return auditEntries.subList(fromIndex, endIndex);
    }

    public void incrementPageNumber() {
        setCurrentPageNumber(Math.min(++currentPageNumber, getNumberOfPages()));
    }

    public void decrementPageNumber() {
        setCurrentPageNumber(Math.max(--currentPageNumber, 1));
    }

    public int getNumberOfPages() {
        return (auditEntries == null) ? 0 : (int) Math.ceil(auditEntries.size() / ITEMS_PER_PAGE);
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

}
