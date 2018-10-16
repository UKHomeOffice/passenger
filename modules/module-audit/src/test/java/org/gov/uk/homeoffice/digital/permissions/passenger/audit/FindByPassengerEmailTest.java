package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FindByPassengerEmailTest {

    @Test
    public void shouldFindByPassportNumber() {
        final String emailAddress = "chuck.norris@karate.com";

        final Handle mockHandle = mock(Handle.class);
        final AuditDAO mockAuditDAO = mock(AuditDAO.class);

        when(mockHandle.attach(AuditDAO.class)).thenReturn(mockAuditDAO);

        final FindByPassengerEmail underTest = new FindByPassengerEmail(emailAddress);

        underTest.withHandle(mockHandle);

        verify(mockAuditDAO).selectByPassengerEmail(emailAddress);
    }

}