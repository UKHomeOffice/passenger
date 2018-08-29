package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.WithKeycloakUser;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.Role;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter.ParticipantRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.model.ParticipantModelAdapter;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.ParticipantBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParticipantsControllerTest {

    @MockBean
    @Qualifier("audit.admin")
    private AuditService auditService;

    @MockBean
    private ParticipantRecordRepository participantRepository;

    @Autowired
    private ParticipantsController controller;

    @MockBean
    private ParticipantModelAdapter participantModelAdapter;

    @Test
    @WithMockUser(username = "admin@example.gov.uk", roles = {"ADMIN"})
    public void viewParticipants() {
        Map<String, Object> model = new HashMap<>();
        String view = controller.viewParticipants(model);

        assertThat(view, is("participants/participants-table"));
        verify(participantRepository).getAllParticipantsWithVisas();
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "wicu@example.gov.uk", roles = {"WICU"})
    public void viewParticipants_rejectsUsersWithoutRequestedRole() {
        controller.viewParticipants(new HashMap<>());
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void viewParticipants_rejectsUnauthenticatedUsers() {
        controller.viewParticipants(new HashMap<>());
    }

    @Test
    @WithKeycloakUser(username = "admin@example.gov.uk", role = Role.ROLE_ADMIN)
    public void deleteExistingParticipant() {
        String passportNumber = "passportNumber123";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        when(participantRepository.getByPassportNumber(passportNumber))
                .thenReturn(Optional.of(new ParticipantBuilder()
                        .withDefaults()
                        .setId(9876L)
                        .setPassportNumber(passportNumber).createParticipant()));

        String view = controller.delete(passportNumber, authentication);

        assertThat(view, is("redirect:/participants"));
        verify(participantRepository).deleteParticipantWithPassportNumber(passportNumber);
        verify(auditService).audit("action='delete', entity='Participant', id='9876'",
                "SUCCESS", "admin@example.gov.uk");
    }

    @Test
    @WithKeycloakUser(username = "admin@example.gov.uk", role = Role.ROLE_ADMIN)
    public void deleteNonExistingParticipant() {
        String passportNumber = "passportNumber123";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        when(participantRepository.getByPassportNumber(passportNumber)).thenReturn(Optional.empty());

        String view = controller.delete(passportNumber, authentication);

        assertThat(view, is("redirect:/participants"));
        verify(participantRepository).deleteParticipantWithPassportNumber(passportNumber);
        verify(auditService).audit("action='delete', entity='Participant', id='null'",
                "SUCCESS", "admin@example.gov.uk");
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "wicu@example.gov.uk", roles = {"WICU"})
    public void deleteParticipant_rejectsUsersWithoutRequestedRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        controller.delete("passportNumber", authentication);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void deleteParticipant_rejectsUnauthenticatedUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        controller.delete("passportNumber", authentication);
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Import(ParticipantsController.class)
    public static class Config {
    }
}
