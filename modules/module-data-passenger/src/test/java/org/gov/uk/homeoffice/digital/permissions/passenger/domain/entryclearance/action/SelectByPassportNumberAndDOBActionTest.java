package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.EntryClearanceDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class SelectByPassportNumberAndDOBActionTest {

    @Test
    public void shouldSelectByPassportNumberAndDateOfBirth() {
        final String passportNumber = "passportNumber";
        final LocalDate dateOfBirth = LocalDate.of(1980, 1,1);

        final Handle mockHandle = mock(Handle.class);
        final EntryClearanceDAO mockDAO = mock(EntryClearanceDAO.class);

        when(mockHandle.attach(EntryClearanceDAO.class)).thenReturn(mockDAO);

        final SelectByPassportNumberAndDOBAction underTest = new SelectByPassportNumberAndDOBAction(passportNumber, dateOfBirth);
        underTest.withHandle(mockHandle);

        verify(mockDAO).getEntryClearanceByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth);
    }

}