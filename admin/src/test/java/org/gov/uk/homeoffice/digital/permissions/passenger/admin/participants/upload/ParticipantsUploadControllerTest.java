package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs.CrsFileUploadController;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.ParseError;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ParticipantsAdminService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;

import static java.util.Arrays.asList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {
        "visa.datasource=CSV",
        "email.enabled=true"
})
public class ParticipantsUploadControllerTest {
    private static final String CURRENT_USER = "test@test.com";

    @MockBean
    private ParticipantsAdminService participantsAdminService;

    @MockBean
    private CrsFileUploadController crsFileUploadController;
    
    @MockBean
    @Qualifier("audit.admin")
    private AuditService auditService;

    @Autowired
    private ParticipantsUploadController controller;

    private ParsedResult result;

    @Before
    public void setUp() throws Exception {
        Participant participant1 = new ParticipantBuilder().withDefaults()
                .setId(1L).setPassportNumber("ABC")
                .createParticipant();
        Visa visa1 = new VisaBuilder().setPassportNumber("ABC").createVisa();

        Participant participant2 = new ParticipantBuilder().withDefaults()
                .setId(2L).setPassportNumber("DEF")
                .createParticipant();
        Visa visa2 = new VisaBuilder().setStatus(VisaStatus.REVOKED).setPassportNumber("DEF").createVisa();

        ParseError parseError1 = new ParseError("row1", new RuntimeException("it's all gone wrong!"));
        ParseError parseError2 = new ParseError("row2", new RuntimeException("it's all gone wrong!"));

        result = new ParsedResult(asList(tpl(participant1, visa1), tpl(participant2, visa2)), asList(parseError1, parseError2));
    }

    @Test
    @WithKeycloakUser
    public void createAndProcessTempFile() {
        final ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);

        when(participantsAdminService.process(any(File.class), eq(CURRENT_USER))).thenReturn(result);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MultipartFile file = new MockMultipartFile(
            "participants.csv",
            "originalfilename.csv",
            "text/csv",
            "\"some\", \"text\"".getBytes());
        controller.uploadParticipants(file, mock(RedirectAttributes.class), authentication);

        verify(participantsAdminService).process(captor.capture(), eq(CURRENT_USER));
        verify(auditService).audit("action='upload', entity='Participant', fileName='originalfilename.csv', numberOfRecords='2', idRange=[1-2]", "SUCCESS", CURRENT_USER);
        verify(auditService).audit("action='upload', entity='Participant', fileName='originalfilename.csv', row='row1', error='java.lang.RuntimeException: it\\'s all gone wrong!'", "FAILURE", CURRENT_USER);
        verify(auditService).audit("action='upload', entity='Participant', fileName='originalfilename.csv', row='row2', error='java.lang.RuntimeException: it\\'s all gone wrong!'", "FAILURE", CURRENT_USER);
        verify(auditService).audit("action='upload', entity='Participant', fileName='originalfilename.csv', revokedIds=[2]", "SUCCESS", CURRENT_USER);

        File tempFile = captor.getValue();
        assertThat(tempFile.exists(), is(false));
    }

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
