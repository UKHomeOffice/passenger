package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.util.List;
import java.util.Optional;

public interface ParticipantRecordRepository {

    String getType();

    List<Tuple<Participant, Visa>> getAllParticipantsWithVisas();

    Optional<Tuple<Participant, Visa>> getParticipantWithVisa(Long participantId);

    Optional<Participant> getByPassportNumber(String passportNumber);

    void deleteParticipantWithPassportNumber(String passportNumber);

    }
