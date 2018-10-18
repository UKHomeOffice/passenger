package org.gov.uk.homeoffice.digital.permissions.passenger.admin.loginattempts;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginAttemptsService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final AuditService auditService;

    @Autowired
    public LoginAttemptsService(final LoginAttemptRepository loginAttemptRepository,
                                @Qualifier("audit.admin") final AuditService auditService) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.auditService = auditService;
    }

    public List<LoginAttempt> allLoginAttemptsBetween(LocalDateTime from, LocalDateTime to) {
        auditService.audit("action='Read login attempts'", "SUCCESS");
        return loginAttemptRepository.allLoginAttemptsBetween(from, to);
    }

}

