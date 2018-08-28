package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LogUnsuccessfulAttemptActionTest {

    @Test
    public void shouldLogUnsuccessfulAttempts() {
        final String passportNumber = "123ABC456D";
        final String ipAddress = "101.12.122.165";

        final Handle mockHandle = mock(Handle.class);
        final LoginAttemptDAO mockDAO = mock(LoginAttemptDAO.class);

        when(mockHandle.attach(LoginAttemptDAO.class)).thenReturn(mockDAO);

        final LogUnsuccessfulAttemptAction underTest = new LogUnsuccessfulAttemptAction(passportNumber, ipAddress);
        underTest.useHandle(mockHandle);

        verify(mockDAO).logUnsuccessfulAttempt(passportNumber, ipAddress);
    }

}