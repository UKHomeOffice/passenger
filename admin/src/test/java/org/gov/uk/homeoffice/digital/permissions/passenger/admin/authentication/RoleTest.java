package org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RoleTest {

    @Test
    public void shouldHaveCorrectNameForAdmin() {
        assertThat(Role.ROLE_ADMIN.getName(), is("Admin"));
    }

    @Test
    public void shouldHaveCorrectNameForWicu() {
        assertThat(Role.ROLE_WICU.getName(), is("WICU User"));
    }

}