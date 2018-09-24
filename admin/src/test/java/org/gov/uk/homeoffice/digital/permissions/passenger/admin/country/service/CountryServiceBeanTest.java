package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CountryServiceBeanTest {

    @Mock
    private CountryRepository mockCountryRepository;

    @InjectMocks
    private CountryServiceBean underTest;

    @Test
    public void shouldGetCountries() {
        underTest.getCountries();
        verify(mockCountryRepository).findAll();
    }

    @Test
    public void shouldGetCountryByCode() {
        underTest.getCountryByCountryCode("GB");
        verify(mockCountryRepository).findById("GB");
    }

    @Test
    public void shouldSaveCountry() {
        final Country country = new Country();
        underTest.saveCountry(country);
        verify(mockCountryRepository).save(country);
    }

}