package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher.convert;

public class SaveOrUpdateAction implements HandleConsumer<JdbiException> {

    private final Participant participant;

    public SaveOrUpdateAction(final Participant participant) {
        this.participant = participant;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        final RuntimeException exception = new RuntimeException("Participant " + participant + " could not be saved");
        final ParticipantDAO dao = handle.attach(ParticipantDAO.class);
        Boolean result = Optional.ofNullable(dao.exists(participant.getId())).map(id -> {
            convert(() -> dao.update(participant), exception);
            return true;
        }).orElseGet(() -> {
            convert(() -> dao.save(participant), exception);
            return true;
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveOrUpdateAction that = (SaveOrUpdateAction) o;
        return Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant);
    }

}
