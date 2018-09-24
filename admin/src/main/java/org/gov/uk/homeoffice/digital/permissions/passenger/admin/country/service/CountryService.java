package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;

import java.util.Collection;
import java.util.Optional;

public interface CountryService {

    Collection<Country> getCountries();

    Optional<Country> getCountryByCountryCode(String countryCode);

    void saveCountry(Country country);

}
