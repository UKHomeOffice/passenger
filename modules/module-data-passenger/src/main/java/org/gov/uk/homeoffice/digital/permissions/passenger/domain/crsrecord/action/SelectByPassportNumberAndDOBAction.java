package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class SelectByPassportNumberAndDOBAction implements HandleCallback<Optional<CrsRecord>, JdbiException> {

    private final String passportNumber;
    private final LocalDate dateOfBirth;

    public SelectByPassportNumberAndDOBAction(final String passportNumber,
                                              final LocalDate dateOfBirth) {
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public Optional<CrsRecord> withHandle(final Handle handle) throws JdbiException {
        return Optional.ofNullable(handle.attach(CrsRecordDAO.class).getByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectByPassportNumberAndDOBAction that = (SelectByPassportNumberAndDOBAction) o;
        return Objects.equals(passportNumber, that.passportNumber) &&
                Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, dateOfBirth);
    }

}
