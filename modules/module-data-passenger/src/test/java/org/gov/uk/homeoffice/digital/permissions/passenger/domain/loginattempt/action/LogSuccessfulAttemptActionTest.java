package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LogSuccessfulAttemptActionTest {

    @Test
    public void shouldLogSuccessfulAttempts() {
        final String passportNumber = "123ABC456D";
        final String ipAddress = "101.12.122.165";

        final Handle mockHandle = mock(Handle.class);
        final LoginAttemptDAO mockDAO = mock(LoginAttemptDAO.class);

        when(mockHandle.attach(LoginAttemptDAO.class)).thenReturn(mockDAO);

        final LogSuccessfulAttemptAction underTest = new LogSuccessfulAttemptAction(passportNumber, ipAddress);
        underTest.useHandle(mockHandle);

        verify(mockDAO).logSuccessfulAttempt(passportNumber, ipAddress);
    }

}