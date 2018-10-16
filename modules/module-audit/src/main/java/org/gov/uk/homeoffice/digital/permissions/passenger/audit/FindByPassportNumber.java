package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;
import java.util.Objects;

public class FindByPassportNumber implements HandleCallback<Collection<Audit>, JdbiException> {

    private final String passportNumber;

    public FindByPassportNumber(final String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public Collection<Audit> withHandle(Handle handle) throws JdbiException {
        return handle.attach(AuditDAO.class).selectByPassportNumber(passportNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindByPassportNumber that = (FindByPassportNumber) o;
        return Objects.equals(passportNumber, that.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }

}
