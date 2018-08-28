package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MILLIS;

public class DailyWashView {
    public final UUID uuid;
    public final String type;
    public final String filename;
    public final Instant creationTime;
    public final String creatorUsername;
    public final String creatorFullName;
    public final int rows;
    public final Instant lastDownloadTime;
    public final String lastDownloadUsername;
    public final String lastDownloadFullName;

    public DailyWashView(UUID uuid, String type, String filename, Instant creationTime, String creatorUsername, String creatorFullName,
                         int rows, Instant lastDownloadTime, String lastDownloadUsername, String lastDownloadFullName) {
        this.uuid = uuid;
        this.type = type;
        this.filename = filename;
        this.creationTime = creationTime;
        this.creatorUsername = creatorUsername;
        this.creatorFullName = creatorFullName;
        this.rows = rows;
        this.lastDownloadTime = Optional.ofNullable(lastDownloadTime).orElse(null);
        this.lastDownloadUsername = lastDownloadUsername;
        this.lastDownloadFullName = lastDownloadFullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWashView that = (DailyWashView) o;
        return rows == that.rows &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(type, that.type) &&
                Objects.equals(filename, that.filename) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(creatorUsername, that.creatorUsername) &&
                Objects.equals(creatorFullName, that.creatorFullName) &&
                Objects.equals(lastDownloadTime, that.lastDownloadTime) &&
                Objects.equals(lastDownloadUsername, that.lastDownloadUsername) &&
                Objects.equals(lastDownloadFullName, that.lastDownloadFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, type, filename, creationTime, creatorUsername, creatorFullName, rows, lastDownloadTime, lastDownloadUsername, lastDownloadFullName);
    }

    @Override
    public String toString() {
        return "DailyWashView{" +
                "uuid=" + uuid +
                ", type='" + type + '\'' +
                ", filename='" + filename + '\'' +
                ", creationTime=" + creationTime +
                ", creatorUsername='" + creatorUsername + '\'' +
                ", creatorFullName='" + creatorFullName + '\'' +
                ", rows=" + rows +
                ", lastDownloadTime=" + lastDownloadTime +
                ", lastDownloadUsername='" + lastDownloadUsername + '\'' +
                ", lastDownloadFullName='" + lastDownloadFullName + '\'' +
                '}';
    }
}
