package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Visa  implements Serializable {
    private Long id;
    private String passportNumber;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String spx;
    private List<String> catDEndorsements;
    private VisaStatus status;
    private String action;
    private String reason;
    private String visaEndorsement;

    public Visa(Long id,
                String passportNumber,
                LocalDate validFrom,
                LocalDate validTo,
                String spx,
                List<String> catDEndorsements,
                VisaStatus status,
                String action,
                String reason,
                String visaEndorsement) {
        this.id = id;
        this.passportNumber = passportNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.spx = spx;
        this.catDEndorsements = catDEndorsements;
        this.status = status;
        this.action = action;
        this.reason = reason;
        this.visaEndorsement = visaEndorsement;
    }

    public boolean isEmpty() {
        return validFrom == null && validTo == null && spx == null && (catDEndorsements == null || catDEndorsements.isEmpty());
    }

    public Visa withId(Long id) {
        return new Visa(id, passportNumber, validFrom, validTo, spx, catDEndorsements, status, action, reason, visaEndorsement);
    }

    public Visa withCatDEndorsements(List<String> endorsements) {
        return new Visa(id, passportNumber, validFrom, validTo, spx, endorsements, status, action, reason, visaEndorsement);
    }

    public Visa withSpx(String spx) {
        return new Visa(id, passportNumber, validFrom, validTo, spx, catDEndorsements, status, action, reason, visaEndorsement);
    }

    public Visa withStatus(VisaStatus status) {
        return new Visa(id, passportNumber, validFrom, validTo, spx, catDEndorsements, status, action, reason, visaEndorsement);
    }

    public Visa withReason(String reason) {
        return new Visa(id, passportNumber, validFrom, validTo, spx, catDEndorsements, status, action, reason, visaEndorsement);
    }

    public Visa withAction(String action) {
        return new Visa(id, passportNumber, validFrom, validTo, spx, catDEndorsements, status, action, reason, visaEndorsement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visa visa = (Visa) o;

        if (id != null ? !id.equals(visa.id) : visa.id != null) return false;
        if (passportNumber != null ? !passportNumber.equals(visa.passportNumber) : visa.passportNumber != null)
            return false;
        if (validFrom != null ? !validFrom.equals(visa.validFrom) : visa.validFrom != null) return false;
        if (validTo != null ? !validTo.equals(visa.validTo) : visa.validTo != null) return false;
        if (spx != null ? !spx.equals(visa.spx) : visa.spx != null) return false;
        if (catDEndorsements != null ? !catDEndorsements.equals(visa.catDEndorsements) : visa.catDEndorsements != null)
            return false;
        if (status != visa.status) return false;
        return reason != null ? reason.equals(visa.reason) : visa.reason == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (passportNumber != null ? passportNumber.hashCode() : 0);
        result = 31 * result + (validFrom != null ? validFrom.hashCode() : 0);
        result = 31 * result + (validTo != null ? validTo.hashCode() : 0);
        result = 31 * result + (spx != null ? spx.hashCode() : 0);
        result = 31 * result + (catDEndorsements != null ? catDEndorsements.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (visaEndorsement != null ? visaEndorsement.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Visa{" +
                "id=" + id +
                ", passportNumber='" + passportNumber + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", spx='" + spx + '\'' +
                ", catDEndorsements=" + catDEndorsements +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", visaEndorsement='" + visaEndorsement + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public String getSpx() {
        return spx;
    }

    public List<String> getCatDEndorsements() {
        return catDEndorsements;
    }

    public VisaStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getAction() {
        return action;
    }

    public String getVisaEndorsement() {
        return visaEndorsement;
    }

}
