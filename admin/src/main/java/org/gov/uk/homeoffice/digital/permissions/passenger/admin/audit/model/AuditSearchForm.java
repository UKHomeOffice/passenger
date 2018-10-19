package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.model;

import com.google.common.base.Strings;
import lombok.Data;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;

@Data
public class AuditSearchForm {

    private String emailAddress;
    private Boolean administratorOnlyEmail;
    private String passportNumber;
    private String name;
    private Iterable<Audit> auditEntries;

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(emailAddress)
                && Strings.isNullOrEmpty(passportNumber)
                && Strings.isNullOrEmpty(name);
    }

}
