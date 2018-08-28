package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class UpdateSentVisasAction implements HandleConsumer<JdbiException> {

    private Participant participant;

    public UpdateSentVisasAction(Participant participant) {
        this.participant = participant;
    }

    @Override
    public void useHandle(Handle handle) throws JdbiException {
        handle.attach(ParticipantDAO.class).setEmailsSent(participant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateSentVisasAction that = (UpdateSentVisasAction) o;
        return Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant);
    }

}
