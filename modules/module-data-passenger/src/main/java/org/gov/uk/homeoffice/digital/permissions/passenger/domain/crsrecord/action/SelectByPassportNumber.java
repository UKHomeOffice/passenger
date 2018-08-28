package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class SelectByPassportNumber implements HandleCallback<Optional<CrsRecord>, JdbiException> {

    private final String passportNumber;

    public SelectByPassportNumber(final String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public Optional<CrsRecord> withHandle(final Handle handle) throws JdbiException {
        return Optional.ofNullable(handle.attach(CrsRecordDAO.class).getByPassportNumber(passportNumber));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectByPassportNumber that = (SelectByPassportNumber) o;
        return Objects.equals(passportNumber, that.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }

}
