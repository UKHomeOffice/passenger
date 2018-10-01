package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SaveCountryTest {

    @Test
    public void shouldUpdateCountry() {
        final Country country = new Country(Locale.CANADA, Boolean.TRUE, "CAN",
                LocalDateTime.now(), LocalDateTime.now());

        final SaveCountry underTest = new SaveCountry(country);
        final Handle mockHandle = mock(Handle.class);
        final CountryDAO mockDao = mock(CountryDAO.class);

        when(mockHandle.attach(CountryDAO.class)).thenReturn(mockDao);
        when(mockDao.findByCountryCode(Locale.CANADA.getCountry())).thenReturn(Optional.of(country));

        underTest.withHandle(mockHandle);

        verify(mockHandle, times(2)).attach(CountryDAO.class);
        verify(mockDao).update(country);
    }

    @Test
    public void shouldInsertCountry() {
        final Country country = new Country(Locale.CANADA, Boolean.TRUE,"CAN",
                LocalDateTime.now(), LocalDateTime.now());

        final SaveCountry underTest = new SaveCountry(country);
        final Handle mockHandle = mock(Handle.class);
        final CountryDAO mockDao = mock(CountryDAO.class);

        when(mockHandle.attach(CountryDAO.class)).thenReturn(mockDao);
        when(mockDao.findByCountryCode(Locale.CANADA.getCountry())).thenReturn(Optional.empty());

        underTest.withHandle(mockHandle);

        verify(mockHandle, times(2)).attach(CountryDAO.class);
        verify(mockDao).insert(country);
    }

}