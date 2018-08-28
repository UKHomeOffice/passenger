package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashContent.Type;
import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.WicuController.RETENTION_DAYS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class WicuControllerTest {

    @MockBean
    private WicuService wicuService;

    @Autowired
    private WicuController wicuController;

    private final List<DailyWashView> files = asList(
            new DailyWashView(UUID.randomUUID(), "DOC", "180302EVISADocWash.csv",
                    Instant.now(), "user1@example.gov.uk", "User One", 123, Instant.now(),
                    "user1@example.gov.uk", "User One"),
            new DailyWashView(UUID.randomUUID(), "NAME", "180302EVISANameWash.csv",
                    Instant.now(), "user2@example.gov.uk", "User One", 123, Instant.now(),
                    "user1@example.gov.uk", "User One")
    );

    private final List<WicuController.ZonedDailyWashView> zonedFiles = files.stream()
            .map(WicuController.ZonedDailyWashView::new)
            .collect(toUnmodifiableList());

    @Test
    @WithMockUser(username = "user@example.com", roles = {"WICU"})
    public void showGeneratedFileList_toWicuUsers() {
        showGeneratedFileListTest();
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"ADMIN"})
    public void showGeneratedFileList_toAdminUsers() {
        showGeneratedFileListTest();
    }

    private void showGeneratedFileListTest() {
        when(wicuService.files(RETENTION_DAYS)).thenReturn(files);

        Model m = mock(Model.class);
        String view = wicuController.list(m);
        assertThat(view, is("wicu/wicu"));

        verify(m).addAttribute("files", zonedFiles);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void showGeneratedFileList_rejectsUnauthenticatedUsers() {
        wicuController.list(mock(Model.class));
    }

    @Test
    @WithKeycloakUser(role = Role.ROLE_WICU, username = "user@example.gov.uk")
    public void generateFile_forWicuUsers() {
        generateFileTest();
    }

    @Test
    @WithKeycloakUser(role = Role.ROLE_ADMIN, username = "user@example.gov.uk")
    public void generateFile_forAdminUsers() {
        generateFileTest();
    }

    private void generateFileTest() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String view = wicuController.generate(authentication, redirectAttributes);
        assertThat(view, is("redirect:wicu"));

        verify(wicuService).generate("user@example.gov.uk", "Users Name");
        verify(redirectAttributes).addFlashAttribute("generation_started", true);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void generateFile_rejectsUnauthenticatedUsers() {
        wicuController.generate(null, null);
    }


    @Test
    @WithKeycloakUser(role = Role.ROLE_WICU, username = "user@example.gov.uk")
    public void fileDownload_forWicuUsers() throws Exception {
        fileDownloadTest();
    }

    @Test
    @WithKeycloakUser(role = Role.ROLE_ADMIN, username = "user@example.gov.uk")
    public void fileDownload_forAdminUsers() throws Exception {
        fileDownloadTest();
    }

    private void fileDownloadTest() throws IOException {
        UUID creationId = UUID.randomUUID();
        Type type = Type.DOC;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String expectedContent = "content";

        DailyWashCreation creation = new DailyWashCreation(1L, UUID.randomUUID(), Instant.now(),
                1,
                "user@example.gov.uk",
                "doc.csv",
                "name.csv",
                "Users Name");
        when(wicuService.get(creationId)).thenReturn(creation);

        when(wicuService.getFile(creation, type, "user@example.gov.uk", "Users Name")).thenReturn(
                new DailyWashContent("doc.csv", Type.DOC, expectedContent));

        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream os = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(os);

        wicuController.download(creationId.toString(), "DOC", authentication, response);

        verify(response).setContentType("text/csv");
        verify(response).setHeader("Content-Disposition", String.format("attachment; filename=\"doc.csv\""));
        verify(response).setHeader("Content-Length", Integer.toString(expectedContent.length()));

        verify(os).write(expectedContent.getBytes(StandardCharsets.UTF_8));
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Import(WicuController.class)
    public static class Config {
    }
}
