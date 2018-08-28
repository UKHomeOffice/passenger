package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.EntryClearanceDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class SelectByPassportNumberAction implements HandleCallback<Optional<EntryClearance>, JdbiException> {

    private final String passportNumber;

    public SelectByPassportNumberAction(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public Optional<EntryClearance> withHandle(Handle handle) throws JdbiException {
        return Optional.ofNullable(handle.attach(EntryClearanceDAO.class).getEntryClearanceByPassportNumber(passportNumber));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectByPassportNumberAction that = (SelectByPassportNumberAction) o;
        return Objects.equals(passportNumber, that.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }

}
