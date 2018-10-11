package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleBasedUrlAuthenticationSuccessHandlerTest {

    @MockBean
    @Qualifier("audit.admin")
    private AuditService auditService;

    @Autowired
    private RoleBasedUrlAuthenticationSuccessHandler successHandler;

    @Test
    @WithKeycloakUser
    public void successfulLoginsAreAudited() throws Exception {
        HttpServletResponse res = mock(HttpServletResponse.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        successHandler.onAuthenticationSuccess(req, res, authentication);

        verify(auditService).audit("action='login'", "SUCCESS", "test@test.com",null, null, null);
    }

    @Configuration
    @Import(RoleBasedUrlAuthenticationSuccessHandler.class)
    public static class Config {
    }
}
