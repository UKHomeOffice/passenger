package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class EntryClearance  implements Serializable {

    private final String passportNumber;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String passportNationality;
    private final String surname;
    private final String otherNames;
    private final String tierType;
    private final LocalDate dateOfBirth;
    private final String vafNumber;
    private final String casNumber;
    private final String spxNumber;
    private final LocalDate visaValidToDate;
    private final String conditionsLine1;
    private final String conditionsLine2;

    EntryClearance() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public EntryClearance(String passportNumber, LocalDate startDate, LocalDate endDate, String passportNationality, String surname, String otherNames, String tierType, LocalDate dateOfBirth, String vafNumber, String casNumber, String spxNumber, LocalDate visaValidToDate, String conditionsLine1, String conditionsLine2) {
        this.passportNumber = passportNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.passportNationality = passportNationality;
        this.surname = surname;
        this.otherNames = otherNames;
        this.tierType = tierType;
        this.dateOfBirth = dateOfBirth;
        this.vafNumber = vafNumber;
        this.casNumber = casNumber;
        this.spxNumber = spxNumber;
        this.visaValidToDate = visaValidToDate;
        this.conditionsLine1 = conditionsLine1;
        this.conditionsLine2 = conditionsLine2;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getPassportNationality() {
        return passportNationality;
    }

    public String getSurname() {
        return surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public String getTierType() {
        return tierType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getVafNumber() {
        return vafNumber;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public String getSpxNumber() {
        return spxNumber;
    }

    public LocalDate getVisaValidToDate() {
        return visaValidToDate;
    }

    public String getConditionsLine1() {
        return conditionsLine1;
    }

    public String getConditionsLine2() {
        return conditionsLine2;
    }

    public String getFullName(){
        return otherNames + " " + surname;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryClearance that = (EntryClearance) o;

        if (passportNumber != null ? !passportNumber.equals(that.passportNumber) : that.passportNumber != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (passportNationality != null ? !passportNationality.equals(that.passportNationality) : that.passportNationality != null)
            return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (otherNames != null ? !otherNames.equals(that.otherNames) : that.otherNames != null) return false;
        if (tierType != null ? !tierType.equals(that.tierType) : that.tierType != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(that.dateOfBirth) : that.dateOfBirth != null) return false;
        if (vafNumber != null ? !vafNumber.equals(that.vafNumber) : that.vafNumber != null) return false;
        if (casNumber != null ? !casNumber.equals(that.casNumber) : that.casNumber != null) return false;
        if (spxNumber != null ? !spxNumber.equals(that.spxNumber) : that.spxNumber != null) return false;
        if (visaValidToDate != null ? !visaValidToDate.equals(that.visaValidToDate) : that.visaValidToDate != null)
            return false;
        if (conditionsLine1 != null ? !conditionsLine1.equals(that.conditionsLine1) : that.conditionsLine1 != null)
            return false;
        return conditionsLine2 != null ? conditionsLine2.equals(that.conditionsLine2) : that.conditionsLine2 == null;
    }

    @Override
    public int hashCode() {
        int result = passportNumber != null ? passportNumber.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (passportNationality != null ? passportNationality.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (otherNames != null ? otherNames.hashCode() : 0);
        result = 31 * result + (tierType != null ? tierType.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (vafNumber != null ? vafNumber.hashCode() : 0);
        result = 31 * result + (casNumber != null ? casNumber.hashCode() : 0);
        result = 31 * result + (spxNumber != null ? spxNumber.hashCode() : 0);
        result = 31 * result + (visaValidToDate != null ? visaValidToDate.hashCode() : 0);
        result = 31 * result + (conditionsLine1 != null ? conditionsLine1.hashCode() : 0);
        result = 31 * result + (conditionsLine2 != null ? conditionsLine2.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Surname: " + surname + "; Passport: " + passportNumber;

    }


}
