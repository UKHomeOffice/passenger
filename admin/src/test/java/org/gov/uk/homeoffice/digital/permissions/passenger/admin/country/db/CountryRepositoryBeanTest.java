package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action.FindAllCountries;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action.FindCountryById;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action.SaveCountry;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CountryRepositoryBeanTest {

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private CountryRepositoryBean underTest;

    @Test
    public void shouldFindById() {
        String countryCode = "AB";
        underTest.findById(countryCode);
        verify(mockJdbi).withHandle(new FindCountryById(countryCode));
    }

    @Test
    public void shouldFindAll() {
        underTest.findAll();
        verify(mockJdbi).withHandle(new FindAllCountries());
    }

    @Test
    public void shouldSave() {
        Country country = new Country();
        underTest.save(country);
        verify(mockJdbi).inTransaction(new SaveCountry(country));
    }

}