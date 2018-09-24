package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FindCountryByIdTest {

    @Test
    public void shouldFindCountryById() {
        final String id = "AB";
        final FindCountryById underTest = new FindCountryById(id);

        final Handle mockHandle = mock(Handle.class);
        final CountryDAO mockDao = mock(CountryDAO.class);

        when(mockHandle.attach(CountryDAO.class)).thenReturn(mockDao);

        underTest.withHandle(mockHandle);

        verify(mockHandle).attach(CountryDAO.class);
        verify(mockDao).findByCountryCode(id);
    }

}