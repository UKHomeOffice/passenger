package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class SaveVisaAction implements HandleConsumer<JdbiException> {

    private Visa visa;

    public SaveVisaAction(Visa visa) {
        this.visa = visa;
    }

    @Override
    public void useHandle(Handle handle) throws JdbiException {
        final VisaDAO dao = handle.attach(VisaDAO.class);
        Boolean result = Optional.ofNullable(dao.exists(visa.getPassportNumber())).map(visaId -> {
            dao.update(visa.withId(visaId));
            dao.deleteEndorsementsForVisa(visaId);
            visa.getCatDEndorsements().forEach(endorsement -> dao.saveEndorsement(endorsement, visaId));
            return true;
        }).orElseGet(() -> {
            final Long visaId = dao.save(visa);
            visa.getCatDEndorsements().forEach(endorsement -> dao.saveEndorsement(endorsement, visaId));
            return true;
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveVisaAction that = (SaveVisaAction) o;
        return Objects.equals(visa, that.visa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visa);
    }

}
