package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Component
public class VisaAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisaAuthenticationProvider.class);

    private final LoginAttemptRepository loginAttemptRepository;
    private final VisaRecordService visaRecordService;
    private static final int LOCKOUT_PERIOD_IN_MINS = 5;
    private final AuditService auditService;

    public VisaAuthenticationProvider(LoginAttemptRepository loginAttemptRepository,
                                      VisaRecordService visaRecordService,
                                      @Qualifier("audit.public") AuditService auditService) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.visaRecordService = visaRecordService;
        this.auditService = auditService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String passportNumber = authentication.getName();
        if (isLocked(passportNumber)) {
            auditService.auditForPublicUser(String.format("action='login', passportNumber='%s', IPAddress='%s', reason='locked'",
                    passportNumber, RemoteIPThreadLocal.get()), "FAILED",  null, null,
                    passportNumber);
            throw new LockedException("account locked");
        }

        String dateOfBirthString = authentication.getCredentials().toString();
        try {
            final LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString, DateTimeFormatter.ofPattern("d M yyyy"));
            return visaRecordService.getVisaIdentifier(passportNumber, dateOfBirth)
                    .map(Long::valueOf)
                    .map(id -> success(id, passportNumber, dateOfBirth))
                    .orElseGet(() -> failure(passportNumber));
        } catch (DateTimeParseException e) {
            LOGGER.warn("Exception thrown during authentication", e);
            return failure(passportNumber);
        }
    }

    private boolean isLocked(String passportNumber) {
        final List<LoginAttempt> loginAttempts = loginAttemptRepository.loginAttempts(passportNumber, LocalDateTime.now().minusMinutes(LOCKOUT_PERIOD_IN_MINS));
        if (loginAttempts.isEmpty()) {
            return false;
        }
        int failureCounts = 0;
        for (int i = 0; i < loginAttempts.size(); i++) {
            if (!loginAttempts.get(i).success) {
                failureCounts++;
            } else {
                failureCounts = 0;
            }
            if (failureCounts == 3) {
                return true;
            }
        }
        return false;
    }

    private UsernamePasswordAuthenticationToken success(final Long passengerId, final String passportNumber, final LocalDate dateOfBirth) {
        loginAttemptRepository.logSuccessfulAttempt(passportNumber, RemoteIPThreadLocal.get());
        VisaRecord record = visaRecordService.get(String.valueOf(passengerId));
        String passengerName = record.firstValueAsStringFor(VisaRuleConstants.FULL_NAME);
        String passengerEmail = record.firstValueAsStringFor(VisaRuleConstants.EMAIL_ADDRESS);
        auditService.auditForPublicUser(String.format("action='login', passportNumber='%s', IPAddress='%s'",
                passportNumber, RemoteIPThreadLocal.get()), "SUCCESS", passengerName, passengerEmail, passportNumber);
        return new UsernamePasswordAuthenticationToken(passengerId, dateOfBirth, Collections.emptyList());
    }

    private UsernamePasswordAuthenticationToken failure(String passportNumber) {
        loginAttemptRepository.logFailedAttempt(passportNumber, RemoteIPThreadLocal.get());
        auditService.auditForPublicUser(String.format("action='login', passportNumber='%s', IPAddress='%s'", passportNumber, RemoteIPThreadLocal.get()), "FAILED",
                "unknown", null,  passportNumber);
        if (isLocked(passportNumber)) throw new LockedException("account locked");
        throw new BadCredentialsException("Invalid passport/dob combination");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }

}
