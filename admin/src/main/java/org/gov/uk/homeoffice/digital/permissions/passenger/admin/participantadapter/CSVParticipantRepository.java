package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepositoryBean;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CSVParticipantRepository implements ParticipantRecordRepository {

    @Autowired
    ParticipantRepositoryBean participantRepositoryBean;

    @Override
    public String getType() {
        return "CSV";
    }

    @Override
    public List<Tuple<Participant, Visa>> getAllParticipantsWithVisas() {
        return participantRepositoryBean.getAllParticipantsWithVisas();
    }

    @Override
    public Optional<Tuple<Participant, Visa>> getParticipantWithVisa(Long participantId) {
        return participantRepositoryBean.getParticipantWithVisa(participantId);
    }

    @Override
    public Optional<Participant> getByPassportNumber(String passportNumber) {
        return participantRepositoryBean.getByPassportNumber(passportNumber);
    }

    @Override
    public void deleteParticipantWithPassportNumber(String passportNumber) {
        participantRepositoryBean.deleteParticipantWithPassportNumber(passportNumber);
    }
}
