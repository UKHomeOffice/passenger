package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;

public class SelectParticipantWithVisaAction implements HandleCallback<Tuple<Participant, Visa>, JdbiException> {

    private final Long participantId;
    private final VisaRepository visaRepository;

    public SelectParticipantWithVisaAction(final Long participantId, final VisaRepository visaRepository) {
        this.participantId = participantId;
        this.visaRepository = visaRepository;
    }

    @Override
    public Tuple<Participant, Visa> withHandle(Handle handle) throws JdbiException {
        return Optional.ofNullable(handle.attach(ParticipantDAO.class).getById(participantId))
                .map(p -> tpl(p, visaRepository.getByPassportNumber(p.getPassportNumber()).orElse(null)))
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectParticipantWithVisaAction that = (SelectParticipantWithVisaAction) o;
        return Objects.equals(participantId, that.participantId) &&
                Objects.equals(visaRepository, that.visaRepository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, visaRepository);
    }

}
