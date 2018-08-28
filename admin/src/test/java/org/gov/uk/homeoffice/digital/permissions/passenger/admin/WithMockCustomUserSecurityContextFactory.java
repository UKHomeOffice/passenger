package org.gov.uk.homeoffice.digital.permissions.passenger.admin;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithKeycloakUser> {

    @Override
    public SecurityContext createSecurityContext(WithKeycloakUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        AccessToken accessToken = new AccessToken();
        accessToken.setGivenName("Users");
        accessToken.setFamilyName("Name");
        RefreshableKeycloakSecurityContext refreshableKeycloakSecurityContext = new RefreshableKeycloakSecurityContext(null,
                null, UUID.randomUUID().toString(), accessToken, UUID.randomUUID().toString(), null, UUID.randomUUID().toString());
        KeycloakPrincipal principal = new KeycloakPrincipal<>(customUser.username(), refreshableKeycloakSecurityContext);
        Set<String> roles = Set.of(customUser.role().name());
        Authentication auth = new KeycloakAuthenticationToken(new SimpleKeycloakAccount(principal, roles, refreshableKeycloakSecurityContext),
                false, List.of(new SimpleGrantedAuthority(customUser.role().name())));
        context.setAuthentication(auth);
        return context;
    }

}
