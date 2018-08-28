package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

public class ParticipantBuilder {
    private Long id;
    private String gwf;
    private String vaf;
    private String cas;
    private String firstName;
    private String middleName;
    private String surName;
    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String mobileNumber;
    private String email;
    private String institutionAddress;
    private Set<String> emailsSent;
    private String updatedBy;
    private LocalDateTime created;
    private LocalDateTime updated;

    public ParticipantBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public ParticipantBuilder setGwf(String gwf) {
        this.gwf = gwf;
        return this;
    }

    public ParticipantBuilder setVaf(String vaf) {
        this.vaf = vaf;
        return this;
    }

    public ParticipantBuilder setCas(String cas) {
        this.cas = cas;
        return this;
    }

    public ParticipantBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ParticipantBuilder setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public ParticipantBuilder setSurName(String surName) {
        this.surName = surName;
        return this;
    }

    public ParticipantBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public ParticipantBuilder setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public ParticipantBuilder setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public ParticipantBuilder setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public ParticipantBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public ParticipantBuilder setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
        return this;
    }

    public ParticipantBuilder setEmailsSent(Set<String> emailsSent) {
        this.emailsSent = emailsSent;
        return this;
    }

    public ParticipantBuilder setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public ParticipantBuilder setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public ParticipantBuilder setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    public ParticipantBuilder withDefaults() {
        this.id = 1L;
        this.emailsSent = Collections.emptySet();
        this.passportNumber = "passportNumber";
        this.dateOfBirth = LocalDate.of(1997, 1, 1);
        this.surName = "surname";
        return this;
    }

    public Participant createParticipant() {
        return new Participant(id, gwf, vaf, cas, firstName, middleName, surName, dateOfBirth, nationality, passportNumber, mobileNumber, email, institutionAddress, emailsSent, updatedBy, created, updated);
    }


}