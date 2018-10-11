package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminLogoutHandler implements LogoutHandler {

    private final AuditService auditService;

    public AdminLogoutHandler(@Qualifier("audit.admin") AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        auditService.audit("action='logout'", "SUCCESS", null, null, null);
    }
}
