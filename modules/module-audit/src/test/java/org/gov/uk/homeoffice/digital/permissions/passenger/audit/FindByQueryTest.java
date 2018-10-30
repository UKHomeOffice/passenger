package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

public class FindByQueryTest {

    @Test
    public void shouldFindQueryWithDefaultDates() {
        final String emailAddress = "chuck.norris@karate.com";
        final String name = "Chuck Norris";
        final String passportNumber = "46447572";

        final Handle mockHandle = mock(Handle.class);
        final AuditDAO mockAuditDAO = mock(AuditDAO.class);

        when(mockHandle.attach(AuditDAO.class)).thenReturn(mockAuditDAO);

        final FindByQuery underTest = new FindByQuery(emailAddress, emailAddress, passportNumber, name, null, null);

        underTest.withHandle(mockHandle);

        verify(mockAuditDAO).selectByQuery(emailAddress, passportNumber, "%" + name + "%",
                emailAddress,"2000-01-01", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));
    }

}