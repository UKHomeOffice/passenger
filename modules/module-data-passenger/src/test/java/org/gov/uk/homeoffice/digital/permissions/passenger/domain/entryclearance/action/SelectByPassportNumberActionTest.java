package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.EntryClearanceDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SelectByPassportNumberActionTest {

    @Test
    public void shouldSelectByPassportNumber() {
        final String passportNumber = "passportNumber";

        final Handle mockHandle = mock(Handle.class);
        final EntryClearanceDAO mockDAO = mock(EntryClearanceDAO.class);

        when(mockHandle.attach(EntryClearanceDAO.class)).thenReturn(mockDAO);

        final SelectByPassportNumberAction underTest = new SelectByPassportNumberAction(passportNumber);
        underTest.withHandle(mockHandle);

        verify(mockDAO).getEntryClearanceByPassportNumber(passportNumber);
    }

}