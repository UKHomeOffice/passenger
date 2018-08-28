package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface VisaRecordService  {

    VisaRecord get(String id);

    Collection<VisaRecord> getValidWithinRange(LocalDate lowerValidDate, LocalDate upperValidDate);

    Optional<String> getVisaIdentifier(String passportNumber, LocalDate dateOfBirth);

}
