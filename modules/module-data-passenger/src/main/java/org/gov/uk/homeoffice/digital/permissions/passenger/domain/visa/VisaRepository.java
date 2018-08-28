package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;

import java.util.Optional;

public interface VisaRepository {

    void save(Visa visa);

    Optional<Visa> getByPassportNumber(String passportNumber);

}
