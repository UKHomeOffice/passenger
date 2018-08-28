package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.time.LocalDate;
import java.util.List;

public class VisaBuilder {
    private Long id;
    private String passportNumber;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String spx;
    private List<String> catDEndorsements;
    private VisaStatus status;
    private String reason;

    public VisaBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public VisaBuilder setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public VisaBuilder setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public VisaBuilder setValidTo(LocalDate validTo) {
        this.validTo = validTo;
        return this;
    }

    public VisaBuilder setSpx(String spx) {
        this.spx = spx;
        return this;
    }

    public VisaBuilder setCatDEndorsements(List<String> catDEndorsements) {
        this.catDEndorsements = catDEndorsements;
        return this;
    }

    public VisaBuilder setStatus(VisaStatus status) {
        this.status = status;
        return this;
    }

    public VisaBuilder setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Visa createVisa() {
        return new Visa(id, passportNumber, validFrom, validTo, spx, catDEndorsements, status, reason);

    }

}