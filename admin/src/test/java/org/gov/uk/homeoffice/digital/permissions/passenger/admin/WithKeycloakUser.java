package org.gov.uk.homeoffice.digital.permissions.passenger.admin;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithKeycloakUser {

    static final String CURRENT_USER = "test@test.com";

    String username() default CURRENT_USER;

    Role role() default Role.ROLE_ADMIN;

}
