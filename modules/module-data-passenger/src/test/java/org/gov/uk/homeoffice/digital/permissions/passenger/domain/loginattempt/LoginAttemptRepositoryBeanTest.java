package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.GetAttemptsSinceAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.LogSuccessfulAttemptAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.LogUnsuccessfulAttemptAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.SelectAllLoginAttemptsBetween;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptRepositoryBeanTest {

    private static final String PASSPORT_NUMBER = "123ABC456A";

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private LoginAttemptRepositoryBean underTest;

    @Test
    public void shouldLogFailedAttempt() {
        final String ipAddress = "123.12.76.121";
        underTest.logFailedAttempt(PASSPORT_NUMBER, ipAddress);
        verify(mockJdbi).useTransaction(new LogUnsuccessfulAttemptAction(PASSPORT_NUMBER, ipAddress));
    }

    @Test
    public void shouldLogSuccessfulAttempt() {
        final String ipAddress = "123.12.76.121";
        underTest.logSuccessfulAttempt(PASSPORT_NUMBER, ipAddress);
        verify(mockJdbi).useTransaction(new LogSuccessfulAttemptAction(PASSPORT_NUMBER, ipAddress));
    }

    @Test
    public void shouldLoginAttempts() {
        final LocalDateTime since = LocalDateTime.now();
        underTest.loginAttempts(PASSPORT_NUMBER, since);
        verify(mockJdbi).inTransaction(new GetAttemptsSinceAction(PASSPORT_NUMBER, since));
    }

    @Test
    public void shouldAllLoginAttemptsBetween() {
        final LocalDateTime from = LocalDateTime.now();
        final LocalDateTime to = LocalDateTime.now().plusMinutes(10);
        underTest.allLoginAttemptsBetween(from, to);
        verify(mockJdbi).withHandle(new SelectAllLoginAttemptsBetween(from, to));
    }

}