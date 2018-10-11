package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Component
public class RoleBasedUrlAuthenticationSuccessHandler
        extends AbstractAuthenticationTargetUrlRequestHandler
        implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final AuditService auditService;

    public RoleBasedUrlAuthenticationSuccessHandler(@Qualifier("audit.admin") AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        handle(request, response, authentication);
        auditService.audit("action='login'", "SUCCESS", authentication.getPrincipal().toString(),
                null, null, null);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response,
                          Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to "
                    + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private String determineTargetUrl(Authentication authentication) {
        boolean isWicu = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(Role.ROLE_WICU.toString())) {
                isWicu = true;
                break;
            } else if (grantedAuthority.getAuthority().equals(Role.ROLE_ADMIN.toString())) {
                isAdmin = true;
                break;
            }
        }

        if (isWicu) {
            return "/wicu";
        } else if (isAdmin) {
            return "/";
        } else {
            throw new IllegalStateException("unhandled user role");
        }
    }
}
