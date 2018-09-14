package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.model;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.springframework.stereotype.Component;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.parse;

@Component
public class ParticipantModelAdapter {

    public ParticipantModel from(final Participant participant, final Visa visa, boolean uploaded) {
        return new ParticipantModel(participant.getId(),
                participant.getGwf(),
                participant.getVaf(),
                participant.getCas(),
                participant.getFirstName(),
                participant.getMiddleName(),
                participant.getSurName(),
                ofNullable(participant.getDateOfBirth()).map(val -> parse(val, "d MMMM yyyy")).orElse(null),
                participant.getNationality(),
                participant.getPassportNumber(),
                participant.getMobileNumber(),
                participant.getEmail(),
                participant.getInstitutionAddress(),
                ofNullable(visa).flatMap(val -> ofNullable(val.getValidFrom())).map(val -> parse(val, "d MMMM yyyy")).orElse(null),
                ofNullable(visa).flatMap(val -> ofNullable(val.getValidTo())).map(val ->parse(val, "d MMMM yyyy")).orElse(null),
                ofNullable(visa).map(Visa::getSpx).orElse(null),
                ofNullable(visa).flatMap(val -> ofNullable(val.getCatDEndorsements())).map(val -> String.join("\n", visa.getCatDEndorsements())).orElse(""),
                String.join(", ", ofNullable(participant.getEmailsSent()).orElse(emptySet())),
                ofNullable(visa).map(Visa::getStatus).orElse(null),
                ofNullable(visa).map(Visa::getAction).orElse(null),
                ofNullable(visa).map(Visa::getReason).orElse(null),
                uploaded,
                participant.getUpdatedBy(),
                ofNullable(participant.getCreated()).map(val -> parse(val, "dd MMMM yyyy HH:mm:ss")).orElse(null),
                ofNullable(participant.getUpdated()).map(val -> parse(val, "dd MMMM yyyy HH:mm:ss")).orElse(null)
        );
    }

}
