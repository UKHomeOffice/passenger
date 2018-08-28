package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class SelectByPassportNumberAction implements HandleCallback<Optional<Visa>, JdbiException> {

    private final String passportNumber;

    public SelectByPassportNumberAction(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public Optional<Visa> withHandle(Handle handle) throws JdbiException {
        final VisaDAO dao = handle.attach(VisaDAO.class);
        return Optional.ofNullable(dao.getByPassportNumber(passportNumber))
                .map(visa -> visa.withCatDEndorsements(dao.getEndorsementsForVisa(visa.getId())));
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
