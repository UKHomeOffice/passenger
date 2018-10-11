package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser.CURRENT_USER;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class AdminLogoutHandlerTest {

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AdminLogoutHandler adminLogoutHandler;

    @Test
    @WithKeycloakUser
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        adminLogoutHandler.logout(null, null, authentication);

        verify(auditService).audit("action='logout'", "SUCCESS", null, null, null);
    }
}