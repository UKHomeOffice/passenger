package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.time.LocalDate;

public class EntryClearanceBuilder {
    private String passportNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String passportNationality;
    private String surname;
    private String otherNames;
    private String tierType;
    private LocalDate dateOfBirth;
    private String vafNumber;
    private String casNumber;
    private String spxNumber;
    private LocalDate visaValidToDate;
    private String conditionsLine1;
    private String conditionsLine2;

    public EntryClearanceBuilder setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public EntryClearanceBuilder setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public EntryClearanceBuilder setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public EntryClearanceBuilder setPassportNationality(String passportNationality) {
        this.passportNationality = passportNationality;
        return this;
    }

    public EntryClearanceBuilder setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public EntryClearanceBuilder setOtherNames(String otherNames) {
        this.otherNames = otherNames;
        return this;
    }

    public EntryClearanceBuilder setTierType(String tierType) {
        this.tierType = tierType;
        return this;
    }

    public EntryClearanceBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public EntryClearanceBuilder setVafNumber(String vafNumber) {
        this.vafNumber = vafNumber;
        return this;
    }

    public EntryClearanceBuilder setCasNumber(String casNumber) {
        this.casNumber = casNumber;
        return this;
    }

    public EntryClearanceBuilder setSpxNumber(String spxNumber) {
        this.spxNumber = spxNumber;
        return this;
    }

    public EntryClearanceBuilder setVisaValidToDate(LocalDate visaValidToDate) {
        this.visaValidToDate = visaValidToDate;
        return this;
    }

    public EntryClearanceBuilder setConditionsLine1(String conditionsLine1) {
        this.conditionsLine1 = conditionsLine1;
        return this;
    }

    public EntryClearanceBuilder setConditionsLine2(String conditionsLine2) {
        this.conditionsLine2 = conditionsLine2;
        return this;
    }

    public EntryClearance createEntryClearance() {
        return new EntryClearance(passportNumber, startDate, endDate, passportNationality, surname, otherNames, tierType, dateOfBirth, vafNumber, casNumber, spxNumber, visaValidToDate, conditionsLine1, conditionsLine2);
    }
}