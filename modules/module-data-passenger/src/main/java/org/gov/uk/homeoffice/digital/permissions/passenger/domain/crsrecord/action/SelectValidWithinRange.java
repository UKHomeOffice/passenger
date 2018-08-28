package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

public class SelectValidWithinRange implements HandleCallback<Collection<CrsRecord>, JdbiException> {

    private LocalDate lowerDateIncluded;
    private LocalDate upperDateIncluded;

    public SelectValidWithinRange(LocalDate lowerDateIncluded, LocalDate upperDateIncluded) {
        this.lowerDateIncluded = lowerDateIncluded;
        this.upperDateIncluded = upperDateIncluded;
    }

    @Override
    public Collection<CrsRecord> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(CrsRecordDAO.class).getValidWithinRange(lowerDateIncluded, upperDateIncluded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectValidWithinRange that = (SelectValidWithinRange) o;
        return Objects.equals(lowerDateIncluded, that.lowerDateIncluded) &&
                Objects.equals(upperDateIncluded, that.upperDateIncluded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerDateIncluded, upperDateIncluded);
    }

}
