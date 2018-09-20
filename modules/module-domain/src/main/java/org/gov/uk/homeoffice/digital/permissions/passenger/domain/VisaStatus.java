package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.NonNull;

public enum VisaStatus {

    ISSUED, REFUSED;

    public static VisaStatus parse(@NonNull final String description) {
        return VisaStatus.valueOf(description.toUpperCase());
    }

}
