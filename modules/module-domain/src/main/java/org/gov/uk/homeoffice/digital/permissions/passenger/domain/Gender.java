package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

public enum Gender {
    FEMALE,
    MALE,
    UNKNOWN;

    public static Gender parse(String val) {
        if ("F".equalsIgnoreCase(val.trim())) return FEMALE;
        if ("M".equalsIgnoreCase(val.trim())) return MALE;

        return UNKNOWN;
    }
}
