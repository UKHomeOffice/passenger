package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.UnknownAdapterException;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantRecordRepositoryBean {

    private final ParticipantRecordRepository participantRecordRepository;

    public ParticipantRecordRepositoryBean(@Value("${visa.datasource}") final String visaRecordAdapterName,
                                        final List<ParticipantRecordRepository> participantRepositories) {
        participantRecordRepository = participantRepositories
                .stream()
                .filter(p -> p.getType().equalsIgnoreCase(visaRecordAdapterName))
                .findFirst().orElseThrow(() -> new UnknownAdapterException());
    }


    
    public List<Tuple<Participant, Visa>> getAllParticipantsWithVisas() {
        return participantRecordRepository.getAllParticipantsWithVisas();
    }

    
    public Optional<Tuple<Participant, Visa>> getParticipantWithVisa(Long participantId) {
        return participantRecordRepository.getParticipantWithVisa(participantId);
    }

    
    public Optional<Participant> getByPassportNumber(String passportNumber) {
        return participantRecordRepository.getByPassportNumber(passportNumber);
    }

    
    public void deleteParticipantWithPassportNumber(String passportNumber) {
        participantRecordRepository.deleteParticipantWithPassportNumber(passportNumber);
    }
}
