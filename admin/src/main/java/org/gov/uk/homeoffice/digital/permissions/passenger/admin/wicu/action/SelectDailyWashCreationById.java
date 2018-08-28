package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreation;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreationDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class SelectDailyWashCreationById implements HandleCallback<DailyWashCreation, JdbiException> {

    private long id;

    public SelectDailyWashCreationById(final long id) {
        this.id = id;
    }

    @Override
    public DailyWashCreation withHandle(final Handle handle) throws JdbiException {
        return handle.attach(DailyWashCreationDAO.class).get(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectDailyWashCreationById that = (SelectDailyWashCreationById) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
