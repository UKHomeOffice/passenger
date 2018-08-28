package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class GetAttemptsSinceActionTest {

    @Test
    public void shouldGetAttemptsSinceDate() {
        final String passportNumber = "123ABC456D";
        final LocalDateTime since = LocalDateTime.now();

        final Handle mockHandle = mock(Handle.class);
        final LoginAttemptDAO mockDAO = mock(LoginAttemptDAO.class);

        when(mockHandle.attach(LoginAttemptDAO.class)).thenReturn(mockDAO);

        final GetAttemptsSinceAction underTest = new GetAttemptsSinceAction(passportNumber, since);
        underTest.withHandle(mockHandle);

        verify(mockDAO).attemptsSince(passportNumber, since);
    }

}