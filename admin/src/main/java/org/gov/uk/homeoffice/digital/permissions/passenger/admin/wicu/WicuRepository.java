package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.action.*;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class WicuRepository {

    private final Jdbi dbi;

    public WicuRepository(@Qualifier("accounts.db") final Jdbi dbi) {
        this.dbi = dbi;
    }

    public DailyWashCreation save(DailyWashCreation dailyWashCreation) {
        return dbi.inTransaction(new SaveDailyWashCreationAction(dailyWashCreation));
    }

    public List<DailyWashView> getLatest(int days) {
        return dbi.withHandle(new SelectLatestAction(days));
    }

    public DailyWashCreation get(long id) {
        return dbi.withHandle(new SelectDailyWashCreationById(id));
    }

    public DailyWashCreation get(UUID uuid) {
        return dbi.withHandle(new SelectDailyWashCreationByUUID(uuid));
    }

    public void log(DailyWashDownload dailyWashDownload) {
        dbi.useTransaction(new SaveDailyWashDownload(dailyWashDownload));
    }

}
