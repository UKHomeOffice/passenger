package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisaType implements Serializable {

    private Long id;
    private String name;
    private String notes;
    private Boolean enabled;
    private LocalDateTime created;

    public VisaType() {
        super();
    }

    public static VisaType createVisaType(final String name) {
        return createVisaType(name, null);
    }

    public static VisaType createVisaType(final String name, final String notes) {
        return new VisaType(null, name, notes, Boolean.TRUE, null);
    }

}
