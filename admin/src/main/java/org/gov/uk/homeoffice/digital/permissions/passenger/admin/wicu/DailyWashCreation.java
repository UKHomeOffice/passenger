package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MILLIS;

public class DailyWashCreation {
    public final Long id;
    public final UUID uuid;
    public final Instant creationTime;
    public final int rows;
    public final String creatorUsername;
    public final String documentCheckFilename;
    public final String nameCheckFilename;
    public final String fullName;

    public DailyWashCreation(Long id, UUID uuid, Instant creationTime, int rows, String creatorUsername, String documentCheckFilename, String nameCheckFilename, String fullName) {
        this.id = id;
        this.uuid = uuid;
        this.creationTime = creationTime;
        this.rows = rows;
        this.creatorUsername = creatorUsername;
        this.documentCheckFilename = documentCheckFilename;
        this.nameCheckFilename = nameCheckFilename;
        this.fullName = fullName;
    }

    public DailyWashCreation(UUID uuid, Instant creationTime, int rows, String creatorUsername, String documentCheckFilename, String nameCheckFilename, String fullName) {
        this(null, uuid, creationTime, rows, creatorUsername, documentCheckFilename, nameCheckFilename, fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWashCreation that = (DailyWashCreation) o;
        return rows == that.rows &&
                Objects.equals(id, that.id) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(creatorUsername, that.creatorUsername) &&
                Objects.equals(documentCheckFilename, that.documentCheckFilename) &&
                Objects.equals(nameCheckFilename, that.nameCheckFilename) &&
                Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, creationTime, rows, creatorUsername, documentCheckFilename, nameCheckFilename, fullName);
    }

    @Override
    public String toString() {
        return "DailyWashCreation{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", creationTime=" + creationTime +
                ", rows=" + rows +
                ", creatorUsername='" + creatorUsername + '\'' +
                ", documentCheckFilename='" + documentCheckFilename + '\'' +
                ", nameCheckFilename='" + nameCheckFilename + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
