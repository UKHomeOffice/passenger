package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeleteOlderThanAction implements HandleConsumer<JdbiException> {

    private LocalDateTime dateTime;

    public DeleteOlderThanAction(final LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(CrsRecordDAO.class).deleteOlderThan(dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteOlderThanAction that = (DeleteOlderThanAction) o;
        return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }

}
