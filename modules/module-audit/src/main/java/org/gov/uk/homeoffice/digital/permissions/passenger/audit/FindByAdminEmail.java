package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;
import java.util.Objects;

public class FindByAdminEmail implements HandleCallback<Collection<Audit>, JdbiException> {

    private final String emailAddress;

    public FindByAdminEmail(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public Collection<Audit> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(AuditDAO.class).selectByAdministratorEmail(emailAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindByAdminEmail that = (FindByAdminEmail) o;
        return Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }

}
