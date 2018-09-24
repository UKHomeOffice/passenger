package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action.FindAllCountries;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action.FindCountryById;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action.SaveCountry;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class CountryRepositoryBean implements CountryRepository {

    private final Jdbi jdbi;

    @Autowired
    public CountryRepositoryBean(@Qualifier("accounts.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Collection<Country> findAll() {
        return jdbi.withHandle(new FindAllCountries());
    }

    @Override
    public Optional<Country> findById(final String countryCode) {
        return jdbi.withHandle(new FindCountryById(countryCode));
    }

    @Override
    public void save(final Country country) {
        jdbi.inTransaction(new SaveCountry(country));
    }

}
