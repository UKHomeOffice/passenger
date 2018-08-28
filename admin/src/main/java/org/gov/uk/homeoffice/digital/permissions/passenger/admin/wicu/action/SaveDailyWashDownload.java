package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashDownload;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashDownloadDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class SaveDailyWashDownload implements HandleConsumer<JdbiException> {

    private final DailyWashDownload dailyWashDownload;

    public SaveDailyWashDownload(DailyWashDownload dailyWashDownload) {
        this.dailyWashDownload = dailyWashDownload;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(DailyWashDownloadDAO.class).save(dailyWashDownload);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveDailyWashDownload that = (SaveDailyWashDownload) o;
        return Objects.equals(dailyWashDownload, that.dailyWashDownload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyWashDownload);
    }

}
