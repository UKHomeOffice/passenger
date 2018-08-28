package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;


import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;

import java.time.LocalDate;
import java.util.Optional;

public interface EntryClearanceRepository {

    void createOrUpdate(EntryClearance ec);

    Optional<EntryClearance> getEntryClearanceByPassportNumber(String passportNumber);

    Optional<EntryClearance> getByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth);

}
