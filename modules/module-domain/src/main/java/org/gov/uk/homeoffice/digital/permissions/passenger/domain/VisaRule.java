package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class VisaRule implements Serializable {

    private String rule;
    private Boolean enabled;

    public VisaRule(final String rule) {
        this(rule, true);
    }

}
