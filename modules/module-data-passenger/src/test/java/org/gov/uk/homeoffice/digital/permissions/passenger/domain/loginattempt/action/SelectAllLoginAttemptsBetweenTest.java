package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class SelectAllLoginAttemptsBetweenTest {

    @Test
    public void shouldSelectAllLoginAttempts() {
        final LocalDateTime from = LocalDateTime.now();
        final LocalDateTime to = LocalDateTime.now().plusMinutes(10);

        final Handle mockHandle = mock(Handle.class);
        final LoginAttemptDAO mockDAO = mock(LoginAttemptDAO.class);

        when(mockHandle.attach(LoginAttemptDAO.class)).thenReturn(mockDAO);

        final SelectAllLoginAttemptsBetween underTest = new SelectAllLoginAttemptsBetween(from, to);
        underTest.withHandle(mockHandle);

        verify(mockDAO).allAttemptsBetween(from, to);
    }

}