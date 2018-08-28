package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;

public class SelectParticipantsWithVisasAction implements HandleCallback<List<Tuple<Participant, Visa>>, JdbiException> {

    private final VisaRepository visaRepository;

    public SelectParticipantsWithVisasAction(final VisaRepository visaRepository) {
        this.visaRepository = visaRepository;
    }

    @Override
    public List<Tuple<Participant, Visa>> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(ParticipantDAO.class).getAll().stream()
                .map(participant -> tpl(participant, visaRepository.getByPassportNumber(participant.getPassportNumber()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectParticipantsWithVisasAction that = (SelectParticipantsWithVisasAction) o;
        return Objects.equals(visaRepository, that.visaRepository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visaRepository);
    }

}
