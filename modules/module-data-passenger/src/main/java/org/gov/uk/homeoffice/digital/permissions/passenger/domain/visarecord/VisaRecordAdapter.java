package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface VisaRecordAdapter<ID> {

    default VisaAdapterType getType() {
        return VisaAdapterType.CRS;
    }

    VisaRecord toVisaRecord(ID id);

    Collection<VisaRecord> getValidWithinRange(LocalDate lowerValidDate, LocalDate upperValidDate);

    Optional<ID> getIdentifier(String passportNumber, LocalDate dateOfBirth);

}
