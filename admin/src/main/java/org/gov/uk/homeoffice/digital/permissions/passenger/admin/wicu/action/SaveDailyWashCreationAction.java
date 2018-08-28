package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreation;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreationDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class SaveDailyWashCreationAction implements HandleCallback<DailyWashCreation, JdbiException> {

    private final DailyWashCreation dailyWashCreation;

    public SaveDailyWashCreationAction(DailyWashCreation dailyWashCreation) {
        this.dailyWashCreation = dailyWashCreation;
    }

    @Override
    public DailyWashCreation withHandle(final Handle handle) throws JdbiException {
        return handle.attach(DailyWashCreationDAO.class).save(dailyWashCreation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveDailyWashCreationAction that = (SaveDailyWashCreationAction) o;
        return Objects.equals(dailyWashCreation, that.dailyWashCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyWashCreation);
    }

}
