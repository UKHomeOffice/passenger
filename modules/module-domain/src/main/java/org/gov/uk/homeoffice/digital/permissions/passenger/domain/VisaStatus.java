package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.NonNull;

import java.util.List;

public enum VisaStatus {

    VALID, REVOKED;

    /* Store options for revoked strings, CSV, CRS, etc */
    private static List<String> revokedStrings = List.of("revoke", "revoked");

    public static VisaStatus parse(@NonNull final String description) {
        return revokedStrings.stream().filter(value -> value.equalsIgnoreCase(description))
                .findFirst().map(value -> REVOKED).orElse(VALID);
    }

}
