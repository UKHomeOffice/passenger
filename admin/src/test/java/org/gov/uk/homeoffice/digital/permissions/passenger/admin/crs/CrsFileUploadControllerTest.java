package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.hamcrest.CoreMatchers;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser.CURRENT_USER;
import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus.REVOKED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CrsFileUploadControllerTest {

    @MockBean
    CrsFileUploadService crsFileUploadServiceMock;

    @MockBean
    CrsAuditService crsAuditServiceMock;

    @Autowired
    CrsFileUploadController testObject;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    @WithKeycloakUser
    public void uploadEmptyList() {
        final ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);

        final CrsParsedResult crsParsedResult = new CrsParsedResult(Collections.emptyList(), Collections.emptyList());
        when(crsFileUploadServiceMock.process(any(File.class), eq(CURRENT_USER)))
                .thenReturn(crsParsedResult);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MultipartFile FILE = new MockMultipartFile(
                "crsrecords.csv",
                "originalfilename.csv",
                "text/csv",
                "\"some\", \"text\"".getBytes());
        testObject.uploadCrsRecords(FILE, mock(RedirectAttributes.class), authentication);

        verify(crsFileUploadServiceMock).process(captor.capture(), eq(CURRENT_USER));
        verify(crsAuditServiceMock).audit(FILE, CURRENT_USER, crsParsedResult);

        File tempFile = captor.getValue();
        assertThat(tempFile.exists(), is(false));
    }

    @Test
    @WithKeycloakUser
    public void uploadValidRecords() {
        final ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);

        when(crsFileUploadServiceMock.process(any(File.class), eq(CURRENT_USER)))
                .thenReturn(new CrsParsedResult(
                        List.of(CrsRecord.builder().id(1L).build(),
                                CrsRecord.builder().id(2L).build()),
                        Collections.emptyList()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MultipartFile FILE = new MockMultipartFile(
                "crsrecords.csv",
                "originalfilename.csv",
                "text/csv",
                "\"some\", \"text\"".getBytes());
        testObject.uploadCrsRecords(FILE, mock(RedirectAttributes.class), authentication);

        verify(crsFileUploadServiceMock).process(captor.capture(), eq(CURRENT_USER));
        verify(crsAuditServiceMock).audit(any(), any(), any());

        File tempFile = captor.getValue();
        assertThat(tempFile.exists(), CoreMatchers.is(false));
    }

    @Test
    @WithKeycloakUser
    public void uploadWithErrors() {
        final ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);

        when(crsFileUploadServiceMock.process(any(File.class), eq(CURRENT_USER)))
                .thenReturn(
                        new CrsParsedResult(List.of(
                                CrsRecord.builder().id(1L).build(),
                                CrsRecord.builder().id(2L).build()),
                                List.of(
                                        new CrsParseErrors("2,row,with,error", List.of("message 21", "message 22")),
                                        new CrsParseErrors("4,row,with,error", List.of("message 41")))
                        ));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MultipartFile FILE = new MockMultipartFile(
                "crsrecords.csv",
                "originalfilename.csv",
                "text/csv",
                "\"some\", \"text\"".getBytes());
        testObject.uploadCrsRecords(FILE, mock(RedirectAttributes.class), authentication);

        verify(crsFileUploadServiceMock).process(captor.capture(), eq(CURRENT_USER));

        File tempFile = captor.getValue();
        assertThat(tempFile.exists(), CoreMatchers.is(false));
    }

    @Test
    @WithKeycloakUser
    public void uploadWithRevokedParticipants() {
        final ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);

        when(crsFileUploadServiceMock.process(any(File.class), eq(CURRENT_USER)))
                .thenReturn(
                        new CrsParsedResult(List.of(
                                CrsRecord.builder().id(1L).build(),
                                CrsRecord.builder().id(2L).status(REVOKED).build(),
                                CrsRecord.builder().id(3L).build(),
                                CrsRecord.builder().id(4L).status(REVOKED).build()),
                                List.of()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MultipartFile FILE = new MockMultipartFile(
                "crsrecords.csv",
                "originalfilename.csv",
                "text/csv",
                "\"some\", \"text\"".getBytes());
        testObject.uploadCrsRecords(FILE, mock(RedirectAttributes.class), authentication);

        verify(crsFileUploadServiceMock).process(captor.capture(), eq(CURRENT_USER));

        File tempFile = captor.getValue();
        assertThat(tempFile.exists(), CoreMatchers.is(false));
    }


    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "wicu@example.gov.uk", roles = {"WICU"})
    public void createAndProcessTempFile_rejectsUsersWithoutRequestedRole() {
        testObject.uploadCrsRecords(null, null, null);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void createAndProcessTempFile_rejectsUnauthenticatedUsers() {
        testObject.uploadCrsRecords(null, null, null);
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Import(CrsFileUploadController.class)
    public static class Config {
    }
}
