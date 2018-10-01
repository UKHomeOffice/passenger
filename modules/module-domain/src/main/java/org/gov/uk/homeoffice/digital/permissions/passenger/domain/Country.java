package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
public class Country implements Serializable {

    private String id;
    private Locale country;
    private String exportCountry;
    private Boolean enabled;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Country() {
        super();
    }

    public Country(Locale locale, Boolean enabled, String exportCountry, LocalDateTime created, LocalDateTime updated) {
        this.id = (locale == null) ? null : locale.getCountry();
        this.country = locale;
        this.exportCountry = exportCountry;
        this.enabled = enabled;
        this.created = created;
        this.updated = updated;
    }

    public String getDisplay() {
        return country == null ? null : country.getDisplayCountry();
    }

    public boolean matches(final String identifier) {
        return identifier.equalsIgnoreCase(exportCountry);
    }

}
