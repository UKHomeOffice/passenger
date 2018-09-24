package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;

import java.util.Collection;
import java.util.Optional;

public interface CountryRepository {

    Collection<Country> findAll();

    Optional<Country> findById(String countryCode);

    void save(Country country);

}
