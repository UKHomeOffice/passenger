package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FindAllCountriesTest {

    @InjectMocks
    private FindAllCountries underTest;

    @Test
    public void shouldFindAllCountries() {
        final Handle mockHandle = mock(Handle.class);
        final CountryDAO mockDao = mock(CountryDAO.class);

        when(mockHandle.attach(CountryDAO.class)).thenReturn(mockDao);

        underTest.withHandle(mockHandle);

        verify(mockHandle).attach(CountryDAO.class);
        verify(mockDao).findAll();
    }

}