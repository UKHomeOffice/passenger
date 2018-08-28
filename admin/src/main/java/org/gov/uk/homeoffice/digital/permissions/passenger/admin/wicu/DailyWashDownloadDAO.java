package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface DailyWashDownloadDAO {

    @SqlUpdate("INSERT INTO daily_wash_download (creation_id, type, username, download_time, full_name) " +
            "VALUES (:creationId, :type, :username, :downloadTime, :fullName) " +
            "ON CONFLICT (creation_id, type) DO UPDATE SET username = excluded.username, download_time = excluded.download_time, full_name = excluded.full_name")
    void save(@BindFields DailyWashDownload dailyWashDownload);
}