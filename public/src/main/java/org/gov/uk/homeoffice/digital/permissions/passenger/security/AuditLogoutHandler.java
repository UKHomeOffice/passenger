package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuditLogoutHandler implements LogoutHandler {

    private final AuditService auditService;
    private final ParticipantRepository participantRepository;

    public AuditLogoutHandler(@Qualifier("audit.public") AuditService auditService, ParticipantRepository participantRepository) {
        this.auditService = auditService;
        this.participantRepository = participantRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) return; // logout clicked when the session had expired

        String passengerId = authentication.getName();
        String passportNumber = participantRepository.getById(Long.parseLong(passengerId))
                .map(p -> p.getPassportNumber())
                .orElse("passengerId:" + passengerId);
        String ipAddress = RemoteIPThreadLocal.get();
        auditService.audit(String.format("action='logout', passportNumber='%s', IPAddress='%s'", passportNumber, ipAddress), "SUCCESS", "PASSENGER");
    }
}
