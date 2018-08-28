package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

    void save(Participant participant);

    void deleteParticipantWithPassportNumber(String passportNumber);

    Optional<Participant> getById(long id);

    List<Tuple<Participant, Visa>> getAllParticipantsWithVisas();

    Optional<Tuple<Participant, Visa>> getParticipantWithVisa(Long participantId);

    Optional<Participant> getByPassportNumber(String passportNumber);

    Optional<Participant> getByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth);

    List<Participant> getByVisaValidFrom(LocalDate lowerLimitIncluded, LocalDate upperLimitIncluded);

    void updateVisaEmailsSent(Participant participant);

}
