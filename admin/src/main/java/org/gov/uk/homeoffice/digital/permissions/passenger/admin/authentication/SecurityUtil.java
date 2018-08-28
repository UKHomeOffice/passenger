package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Optional.ofNullable;

public final class SecurityUtil {

    public static String username() {
        return principal().getName();
    }

    public static KeycloakPrincipal principal() {
        return (KeycloakPrincipal) authentication().getPrincipal();
    }

    public static KeycloakSecurityContext securityContext() {
        return principal().getKeycloakSecurityContext();
    }

    public static String fullName() {
        final KeycloakSecurityContext securityContext = securityContext();
        final String givenName = securityContext.getToken().getGivenName();
        final String familyName = securityContext.getToken().getFamilyName();
        return String.join(" ", ofNullable(givenName).orElse(""),
                ofNullable(familyName).orElse(""));
    }

    public static Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
