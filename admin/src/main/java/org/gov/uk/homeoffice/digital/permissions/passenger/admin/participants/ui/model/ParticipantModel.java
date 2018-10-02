package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.model;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;

import java.io.Serializable;

public class ParticipantModel implements Serializable {

    private final long id;
    private final String gwf;
    private final String vaf;
    private final String cas;
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

    ParticipantModel(long id,
                     final String gwf,
                     final String vaf,
                     final String cas,
                     final String firstName,
                     final String middleName,
                     final String surName,
                     final String dateOfBirth,
                     final String nationality,
                     final String passportNumber,
                     final String mobileNumber,
                     final String email,
                     final String institutionAddress,
                     final String validFrom,
                     final String validTo,
                     final String spx,
                     final String catDEndorsements,
                     final String visaEmailSent,
                     final VisaStatus visaStatus,
                     final String action,
                     final String reason,
                     final boolean updated,
                     final String updatedBy,
                     final String created,
                     final String lastUpdated,
                     final String visaEndorsement) {
        this.id = id;
        this.gwf = gwf;
        this.vaf = vaf;
        this.cas = cas;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surName = surName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.institutionAddress = institutionAddress;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.spx = spx;
        this.catDEndorsements = catDEndorsements;
        this.visaEmailSent = visaEmailSent;
        this.visaStatus = visaStatus;
        this.action = action;
        this.reason = reason;
        this.updated = updated;
        this.updatedBy = updatedBy;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.visaEndorsement = visaEndorsement;
    }

    public long getId() {
        return id;
    }

    public String getGwf() {
        return gwf;
    }

    public String getVaf() {
        return vaf;
    }

    public String getCas() {
        return cas;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurName() {
        return surName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getInstitutionAddress() {
        return institutionAddress;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public String getSpx() {
        return spx;
    }

    public String getCatDEndorsements() {
        return catDEndorsements;
    }

    public String getVisaEmailSent() {
        return visaEmailSent;
    }

    public VisaStatus getVisaStatus() {
        return visaStatus;
    }

    public String getReason() {
        return reason;
    }

    public String getAction() {
        return action;
    }

    public boolean isUpdated() {
        return updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getCreated() {
        return created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getVisaEndorsement() {
        return visaEndorsement;
    }

}
