package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;
import java.util.Objects;

public class FindByPassengerName implements HandleCallback<Collection<Audit>, JdbiException> {

    private final String name;

    public FindByPassengerName(final String name) {
        this.name = "%" + name + "%";
    }

    @Override
    public Collection<Audit> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(AuditDAO.class).selectByPassengerName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindByPassengerName that = (FindByPassengerName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
