package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class DeleteParticipantByPassportNumberAction implements HandleConsumer<JdbiException> {

    private final String passportNumber;

    public DeleteParticipantByPassportNumberAction(final String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(ParticipantDAO.class).delete(passportNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteParticipantByPassportNumberAction that = (DeleteParticipantByPassportNumberAction) o;
        return Objects.equals(passportNumber, that.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }

}
