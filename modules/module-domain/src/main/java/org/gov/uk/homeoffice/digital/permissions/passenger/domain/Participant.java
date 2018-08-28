package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.Validate.notNull;


public class Participant implements Serializable {

    private long id;

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
    private String emailsSentString;
    private String updatedBy;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Participant(Long id, String gwf, String vaf, String cas, String firstName, String middleName, String surName, LocalDate dateOfBirth,
                       String nationality, String passportNumber, String mobileNumber, String email, String institutionAddress, Set<String> emailsSent, String updatedBy, LocalDateTime created, LocalDateTime updated) {
        this.id = notNull(id, "id must be provided");
        this.gwf =  gwf;
        this.vaf = vaf;
        this.cas = cas;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surName = notNull(surName, "surname must be provided");
        this.dateOfBirth = notNull(dateOfBirth, "date of birth must be provided");
        this.nationality = nationality;
        this.passportNumber = notNull(passportNumber,"passport number must be provided");
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.institutionAddress = institutionAddress;
        this.emailsSent = emailsSent;
        this.emailsSentString = ofNullable(emailsSent).map(val -> !val.isEmpty() ? String.join(",", val) : null).orElse(null);
        this.updatedBy = updatedBy;
        this.created = created;
        this.updated = updated;
    }

    public Participant withSurname(String surName) {
        return new Participant(id, gwf, vaf, cas, firstName, middleName, surName, dateOfBirth, nationality, passportNumber, mobileNumber, email, institutionAddress, emailsSent, updatedBy, created, updated);

    }

    public Participant withEmailsSent(Set<String> emailsSent) {
        return new Participant(id, gwf, vaf, cas, firstName, middleName, surName, dateOfBirth, nationality, passportNumber, mobileNumber, email, institutionAddress, emailsSent, updatedBy, created, updated);
    }

    public Participant withUpdatedBy(String updatedBy) {
        return new Participant(id, gwf, vaf, cas, firstName, middleName, surName, dateOfBirth, nationality, passportNumber, mobileNumber, email, institutionAddress, emailsSent, updatedBy, created, updated);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return id == that.id &&
                Objects.equals(gwf, that.gwf) &&
                Objects.equals(vaf, that.vaf) &&
                Objects.equals(cas, that.cas) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(surName, that.surName) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(nationality, that.nationality) &&
                Objects.equals(passportNumber, that.passportNumber) &&
                Objects.equals(mobileNumber, that.mobileNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(institutionAddress, that.institutionAddress) &&
                Objects.equals(emailsSent, that.emailsSent) &&
                Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, gwf, vaf, cas, firstName, middleName, surName, dateOfBirth, nationality, passportNumber, mobileNumber, email, institutionAddress, emailsSent, updatedBy);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", gwf='" + gwf + '\'' +
                ", vaf='" + vaf + '\'' +
                ", cas='" + cas + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", surName='" + surName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nationality='" + nationality + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                ", institutionAddress='" + institutionAddress + '\'' +
                ", emailsSent=" + emailsSent +
                ", updatedBy='" + updatedBy + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
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

    public LocalDate getDateOfBirth() {
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

    public Set<String> getEmailsSent() {
        return emailsSent;
    }

    public String getEmailsSentString() {
        return emailsSentString;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGwf(String gwf) {
        this.gwf = gwf;
    }

    public void setVaf(String vaf) {
        this.vaf = vaf;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    public void setEmailsSent(Set<String> emailsSent) {
        this.emailsSent = emailsSent;
    }

    public void setEmailsSentString(String emailsSentString) {
        this.emailsSentString = emailsSentString;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
