package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs.CrsFileUploadController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {
        "visa.datasource=CSV",
        "email.enabled=true"
})
public class ParticipantsUploadControllerTest {
    private static final String CURRENT_USER = "test@test.com";

    @MockBean
    private CrsFileUploadController crsFileUploadController;

    @Autowired
    private ParticipantsUploadController controller;

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "wicu@example.gov.uk", roles = {"WICU"})
    public void createAndProcessTempFile_rejectsUsersWithoutRequestedRole() {
        controller.uploadParticipants(null, null, null);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void createAndProcessTempFile_rejectsUnauthenticatedUsers() {
        controller.uploadParticipants(null, null, null);
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Import(ParticipantsUploadController.class)
    public static class Config {
    }

}
