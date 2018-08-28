package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit;


import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuditControllerTest {

    public static final String USER_NAME = "test";
    public static final String TEAM_NAME = "team";

    @MockBean
    @Qualifier("audit.admin")
    AuditService auditService;

    @Autowired
    AuditController testObject;

    LocalDateTime from = LocalDateTime.of(2010, 12, 1, 0, 0);
    LocalDateTime to = LocalDateTime.of(2011, 12, 1, 0, 0);

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "wicu@example.gov.uk", roles = {"WICU"})
    public void unauthorisedAccess () {
        testObject.exportAuditAsCsv(from, to, USER_NAME, TEAM_NAME);
    }

    @Test
    @WithMockUser(username = "wicu@example.gov.uk", roles = {"AUDIT"})
    public void downloadLog () throws IOException {
        Mockito.when(auditService.exportAuditLog(from.toLocalDate(), to.toLocalDate(), USER_NAME, TEAM_NAME)).thenReturn("test stream");

        ResponseEntity<InputStreamResource> csv = testObject.exportAuditAsCsv(from, to, USER_NAME, TEAM_NAME);

        assertTrue(csv.getStatusCode().is2xxSuccessful());

    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Import(AuditController.class)
    public static class Config {
    }
}
