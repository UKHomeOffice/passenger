package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts.service.LoginAttemptsService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepository;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginAttemptsServiceTest {

    @InjectMocks
    LoginAttemptsService testObject;

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @Mock
    private AuditService mockAuditService;

    @Test
    public void testLoginAttempts() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        testObject.allLoginAttemptsBetween(from, to);

        verify(loginAttemptRepository).allLoginAttemptsBetween(from, to);
        verify(mockAuditService).audit("action='Read login attempts'", "SUCCESS");
    }

}
