package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class DeleteCrsRecordByIdAction implements HandleConsumer<JdbiException> {

    private final Long id;

    public DeleteCrsRecordByIdAction(final Long id) {
        this.id = id;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(CrsRecordDAO.class).delete(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteCrsRecordByIdAction that = (DeleteCrsRecordByIdAction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
