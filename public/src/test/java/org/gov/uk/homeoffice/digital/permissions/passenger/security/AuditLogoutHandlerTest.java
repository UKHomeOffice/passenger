package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.ParticipantBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuditLogoutHandlerTest {
    @Mock
    private AuditService auditService;
    @Mock
    private ParticipantRepository participantRepository;
    @InjectMocks
    private AuditLogoutHandler auditLogoutHandler;

    @Test
    @WithMockUser(username = "12345")
    public void logout() {
        when(participantRepository.getById(12345L))
                .thenReturn(Optional.of(new ParticipantBuilder()
                        .withDefaults()
                        .setPassportNumber("passportNumber123")
                        .createParticipant()));

        RemoteIPThreadLocal.set("123.123.123.123");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        auditLogoutHandler.logout(null, null, authentication);

        verify(auditService).audit("action='logout', passportNumber='passportNumber123', IPAddress='123.123.123.123'", "SUCCESS", "PASSENGER");
    }
}