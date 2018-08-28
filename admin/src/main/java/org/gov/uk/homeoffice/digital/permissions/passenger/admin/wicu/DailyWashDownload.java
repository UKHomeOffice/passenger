package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import java.time.Instant;
import java.util.Objects;

public class DailyWashDownload {
    public final Long creationId;
    public final DailyWashContent.Type type;
    public final String username;
    public final Instant downloadTime;
    public final String fullName;

    public DailyWashDownload(long creationId, DailyWashContent.Type type, String username, String fullName, Instant downloadTime) {
        this.creationId = creationId;
        this.type = type;
        this.username = username;
        this.downloadTime = downloadTime;
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWashDownload that = (DailyWashDownload) o;
        return Objects.equals(creationId, that.creationId) &&
                type == that.type &&
                Objects.equals(username, that.username) &&
                Objects.equals(downloadTime, that.downloadTime) &&
                Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(creationId, type, username, downloadTime, fullName);
    }

    @Override
    public String toString() {
        return "DailyWashDownload{" +
                "creationId=" + creationId +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", downloadTime=" + downloadTime +
                '}';
    }
}
