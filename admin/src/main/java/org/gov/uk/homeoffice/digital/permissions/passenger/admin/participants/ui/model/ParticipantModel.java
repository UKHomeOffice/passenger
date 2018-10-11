package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.model;

import lombok.Data;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;

import java.io.Serializable;

@Data
public class ParticipantModel implements Serializable {

    private final long id;
    private final String gwf;
    private final String vaf;
    private final String cas;
    private final String cos;
    private final String firstName;
    private final String middleName;
    private final String surName;
    private final String dateOfBirth;
    private final String nationality;
    private final String passportNumber;
    private final String mobileNumber;
    private final String email;
    private final String institutionAddress;
    private final String validFrom;
    private final String validTo;
    private final String spx;
    private final String catDEndorsements;
    private final String visaEmailSent;
    private final VisaStatus visaStatus;
    private final String action;
    private final String reason;
    private final boolean updated;
    private final String updatedBy;
    private final String created;
    private final String lastUpdated;
    private final String visaEndorsement;



}
