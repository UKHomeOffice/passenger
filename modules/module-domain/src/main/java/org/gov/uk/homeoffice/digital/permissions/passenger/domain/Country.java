package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
public class Country implements Serializable {

    private String id;
    private Locale country;
    private Boolean enabled;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Country() {
        super();
    }

    public Country(Locale locale, Boolean enabled, LocalDateTime created, LocalDateTime updated) {
        this.id = (locale == null) ? null : locale.getCountry();
        this.country = locale;
        this.enabled = enabled;
        this.created = created;
        this.updated = updated;
    }

    public String getDisplay() {
        return country == null ? null : country.getDisplayCountry();
    }

}
