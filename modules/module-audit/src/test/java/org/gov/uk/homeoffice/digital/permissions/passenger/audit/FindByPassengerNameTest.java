package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FindByPassengerNameTest {

    @Test
    public void shouldFindByPassengerName() {
        final String name = "Chuck Norris";

        final Handle mockHandle = mock(Handle.class);
        final AuditDAO mockAuditDAO = mock(AuditDAO.class);

        when(mockHandle.attach(AuditDAO.class)).thenReturn(mockAuditDAO);

        final FindByPassengerName underTest = new FindByPassengerName(name);

        underTest.withHandle(mockHandle);

        verify(mockAuditDAO).selectByPassengerName("%" + name + "%");
    }

}