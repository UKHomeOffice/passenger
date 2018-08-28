package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreation;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreationDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.UUID;

public class SelectDailyWashCreationByUUID implements HandleCallback<DailyWashCreation, JdbiException> {

    private UUID uuid;

    public SelectDailyWashCreationByUUID(final UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public DailyWashCreation withHandle(Handle handle) throws JdbiException {
        return handle.attach(DailyWashCreationDAO.class).get(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectDailyWashCreationByUUID that = (SelectDailyWashCreationByUUID) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
