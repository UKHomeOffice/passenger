package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class RemoveVisaTypeAction implements HandleConsumer<JdbiException> {

    private Long id;

    public RemoveVisaTypeAction(final Long id) {
        this.id = id;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(VisaTypeDAO.class).delete(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveVisaTypeAction that = (RemoveVisaTypeAction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
