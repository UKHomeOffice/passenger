package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs.CrsFileUploadService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

@Component
public class CRSParticipantRepository implements ParticipantRecordRepository {

    @Autowired
    private CrsRecordRepository crsRecordRepository;

    @Override
    public String getType() {
        return "CRS";
    }

    @Override
    public List<Tuple<Participant, Visa>> getAllParticipantsWithVisas() {
        return crsRecordRepository.getAll().map(crsRecords -> crsRecords
                .stream()
                .map(crsRecord -> getParticipantVisaTuple(crsRecord)).collect(Collectors.toList())).orElse(emptyList());
    }

    @Override
    public Optional<Tuple<Participant, Visa>> getParticipantWithVisa(Long participantId) {
        return crsRecordRepository.getById(participantId).map(crsRecord -> getParticipantVisaTuple(crsRecord));
    }

    @Override
    public Optional<Participant> getByPassportNumber(String passportNumber) {
        return crsRecordRepository.getByPassportNumber(passportNumber).map(crsRecord -> getParticipant(crsRecord));
    }

    @Override
    public void deleteParticipantWithPassportNumber(String passportNumber) {
        crsRecordRepository.getByPassportNumber(passportNumber).ifPresent(crsRecord -> crsRecordRepository.deleteById(crsRecord.getId()));
    }

    private Tuple<Participant, Visa> getParticipantVisaTuple(CrsRecord crsRecord) {
        Participant participant = getParticipant(crsRecord);
        Visa visa = new Visa(null, crsRecord.getPassportNumber(), crsRecord.getValidFrom(), crsRecord.getValidTo(), crsRecord.getSponsorSpxNo(), Arrays.asList(crsRecord.getCatDEndors1(), crsRecord.getCatDEndors2()), crsRecord.getStatus(), null);
        return new Tuple<>(participant, visa);
    }

    private Participant getParticipant(CrsRecord crsRecord) {
        return new Participant(crsRecord.getId(),
                crsRecord.getGwfRef(),
                crsRecord.getVafNo(),
                crsRecord.getCasNo(),
                crsRecord.getOtherName(),
                null,
                crsRecord.getFamilyName(),
                crsRecord.getDateOfBirth(),
                crsRecord.getNationality(),
                crsRecord.getPassportNumber(),
                crsRecord.getMobileNumber(),
                crsRecord.getEmailAddress(),
                crsRecord.getSponsorAddress(),
                crsRecord.getEmailsSent(),
                crsRecord.getUpdatedBy(),
                null,
                crsRecord.getUpdated());
    }

}
