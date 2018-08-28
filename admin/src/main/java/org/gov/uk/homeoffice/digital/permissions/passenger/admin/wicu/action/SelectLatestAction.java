package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashCreationDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashView;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.List;

public class SelectLatestAction implements HandleCallback<List<DailyWashView>, JdbiException> {

    private int days;

    public SelectLatestAction(int days) {
        this.days = days;
    }

    @Override
    public List<DailyWashView> withHandle(Handle handle) throws JdbiException {
        return handle.attach(DailyWashCreationDAO.class).getLatest(days);
    }

}
