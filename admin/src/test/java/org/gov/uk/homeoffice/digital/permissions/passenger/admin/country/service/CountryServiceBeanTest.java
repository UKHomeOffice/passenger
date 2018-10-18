package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.service;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
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

    @Mock
    private AuditService mockAuditService;

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
    public void shouldSaveCountryWithAuditMessageWithEnabledTrue() {
        final Country country = new Country();
        country.setExportCountry("GBR");
        country.setEnabled(Boolean.TRUE);
        underTest.saveCountry(country);
        verify(mockCountryRepository).save(country);
        verify(mockAuditService).audit("action='Update Country (GBR), enabled=true'", "SUCCESS");
    }

    @Test
    public void shouldSaveCountryWithAuditMessageWithEnabledFalse() {
        final Country country = new Country();
        country.setExportCountry("GBR");
        country.setEnabled(Boolean.FALSE);
        underTest.saveCountry(country);
        verify(mockCountryRepository).save(country);
        verify(mockAuditService).audit("action='Update Country (GBR), enabled=false'", "SUCCESS");
    }

}