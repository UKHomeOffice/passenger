package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ParticipantRepositoryBean implements ParticipantRepository {

    private final Jdbi jdbi;
    private final VisaRepository visaRepository;

    public ParticipantRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi,
                                     final VisaRepository visaRepository) {
        this.jdbi = jdbi;
        this.visaRepository = visaRepository;
    }

    public void save(final Participant participant) {
        jdbi.useTransaction(new SaveOrUpdateAction(participant));
    }

    public void deleteParticipantWithPassportNumber(String passportNumber) {
        jdbi.useTransaction(new DeleteParticipantByPassportNumberAction(passportNumber));
    }

    public Optional<Participant> getById(long id) {
        return jdbi.withHandle(new SelectByIdAction(id));
    }

    public List<Tuple<Participant, Visa>> getAllParticipantsWithVisas() {
        return jdbi.withHandle(new SelectParticipantsWithVisasAction(visaRepository));
    }

    public Optional<Tuple<Participant, Visa>> getParticipantWithVisa(Long participantId) {
        return Optional.ofNullable(jdbi.withHandle(new SelectParticipantWithVisaAction(participantId, visaRepository)));
    }

    public Optional<Participant> getByPassportNumber(String passportNumber) {
        return jdbi.withHandle(new SelectByPassportNumberAction(passportNumber));
    }

    public Optional<Participant> getByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth) {
        return jdbi.withHandle(new SelectByPassportNumberAndDOBAction(passportNumber, dateOfBirth));
    }

    public List<Participant> getByVisaValidFrom(LocalDate lowerLimitIncluded, LocalDate upperLimitIncluded) {
        return jdbi.withHandle(new SelectVisaByValidFromAction(lowerLimitIncluded, upperLimitIncluded));
    }

    public void updateVisaEmailsSent(Participant participant) {
        jdbi.useTransaction(new UpdateSentVisasAction(participant));
    }

}
