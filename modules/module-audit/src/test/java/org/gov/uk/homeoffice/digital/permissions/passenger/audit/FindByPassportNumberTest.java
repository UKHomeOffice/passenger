package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FindByPassportNumberTest {

    @Test
    public void shouldFindByPassportNumber() {
        final String passportNumber = "1232138892";

        final Handle mockHandle = mock(Handle.class);
        final AuditDAO mockAuditDAO = mock(AuditDAO.class);

        when(mockHandle.attach(AuditDAO.class)).thenReturn(mockAuditDAO);

        final FindByPassportNumber underTest = new FindByPassportNumber(passportNumber);

        underTest.withHandle(mockHandle);

        verify(mockAuditDAO).selectByPassportNumber(passportNumber);
    }

}