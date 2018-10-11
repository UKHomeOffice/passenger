package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.authentication.RemoteIPThreadLocal;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisaAuthenticationProviderTest {

    @Mock
    AuditService auditService;

    @Mock
    VisaRecordService visaRecordService;

    @Mock
    LoginAttemptRepository loginAttemptRepository;

    @InjectMocks
    VisaAuthenticationProvider visaAuthenticationProvider;

    @Test
    public void returnAuthenticationTokenIfParticipantAndVisaExistsForPassportAndDateOfBirthCombination() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";
        final String participantId = "1234";
        RemoteIPThreadLocal.set(ipAddress);
        final LocalDate dateOfBirth = LocalDate.of(1997, 2, 1);
        final Authentication authentication = getAuthentication(passportNumber, "1 2 1997");

        when(visaRecordService.get(participantId)).thenReturn(new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("VisaType"),
                List.of(
                        new Tuple<>(new VisaRule(VisaRuleConstants.FULL_NAME), List.of(new VisaRuleContent(1L, VisaRuleConstants.EMAIL_ADDRESS, "Jim SMITH", true, RuleType.USER_DATA))),
                        new Tuple<>(new VisaRule(VisaRuleConstants.EMAIL_ADDRESS), List.of(new VisaRuleContent(1L, VisaRuleConstants.EMAIL_ADDRESS, "jimsmith@test.com", true, RuleType.USER_DATA)))
                )));
        when(visaRecordService.getVisaIdentifier(passportNumber, dateOfBirth)).thenReturn(Optional.of(participantId));

        final Authentication authenticationToken = visaAuthenticationProvider.authenticate(authentication);

        assertThat(authenticationToken, is(instanceOf(UsernamePasswordAuthenticationToken.class)));
        assertThat(authenticationToken.getPrincipal(), is(Long.valueOf(participantId)));
        assertThat(authenticationToken.isAuthenticated(), is(true));

        verify(loginAttemptRepository).logSuccessfulAttempt(passportNumber, ipAddress);
        verify(auditService).audit("action='login', passportNumber='passportNumber', IPAddress='123.123.123.123'", "SUCCESS",
                "jimsmith@test.com", "Jim SMITH", "jimsmith@test.com", passportNumber);
    }

    @Test
    public void throwBadCredentialsExceptionIfParticipantDoesNotExist() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";
        RemoteIPThreadLocal.set(ipAddress);
        final LocalDate dateOfBirth = LocalDate.of(1997, 12, 10);
        final Authentication authentication = getAuthentication(passportNumber, "10 12 1997");

        when(visaRecordService.getVisaIdentifier(passportNumber, dateOfBirth)).thenReturn(Optional.empty());

        try {
            visaAuthenticationProvider.authenticate(authentication);
            fail("should not reach this");
        } catch (BadCredentialsException expected) {
        }

        verify(auditService)
                .audit("action='login', passportNumber='passportNumber', IPAddress='123.123.123.123'", "FAILED", "unknown",null,
                        null, passportNumber);
    }

    @Test
    public void throwLockedExceptionIfThreeUnsuccessfulAttemptsInARowInLastTenMinutes() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";
        RemoteIPThreadLocal.set(ipAddress);

        final LoginAttempt attempt1 = new LoginAttemptBuilder().setPassportNumber(passportNumber).setTime(LocalDateTime.now().minusMinutes(1)).setSuccess(false).createLoginAttempt();
        final LoginAttempt attempt2 = new LoginAttemptBuilder().setPassportNumber(passportNumber).setTime(LocalDateTime.now().minusMinutes(2)).setSuccess(false).createLoginAttempt();
        final LoginAttempt attempt3 = new LoginAttemptBuilder().setPassportNumber(passportNumber).setTime(LocalDateTime.now().minusMinutes(3)).setSuccess(false).createLoginAttempt();
        final LoginAttempt attempt4 = new LoginAttemptBuilder().setPassportNumber(passportNumber).setTime(LocalDateTime.now().minusMinutes(4)).setSuccess(true).createLoginAttempt();
        final LoginAttempt attempt5 = new LoginAttemptBuilder().setPassportNumber(passportNumber).setTime(LocalDateTime.now().minusMinutes(5)).setSuccess(false).createLoginAttempt();
        when(loginAttemptRepository.loginAttempts(eq(passportNumber), any(LocalDateTime.class)))
                .thenReturn(asList(attempt1, attempt2, attempt3, attempt4, attempt5));

        final Authentication authentication = getAuthentication(passportNumber, "10 12 1997");

        try {
            visaAuthenticationProvider.authenticate(authentication);
            fail("should not reach this");
        } catch (LockedException expected) {
        }

        verify(auditService)
                .audit("action='login', passportNumber='passportNumber', IPAddress='123.123.123.123', reason='locked'", "FAILED",
                        null, null, passportNumber);
    }

    @Test
    public void logFaileAttemptIfParticipantDoesNotExist() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";
        RemoteIPThreadLocal.set(ipAddress);
        final LocalDate dateOfBirth = LocalDate.of(1997, 12, 10);
        final Authentication authentication = getAuthentication(passportNumber, "10 12 1997");

        when(visaRecordService.getVisaIdentifier(passportNumber, dateOfBirth)).thenReturn(Optional.empty());

        try {
            visaAuthenticationProvider.authenticate(authentication);
            fail("should not reach this");
        } catch (AuthenticationException expected) {
        }

        verify(loginAttemptRepository).logFailedAttempt(passportNumber, ipAddress);
        verify(auditService).audit("action='login', passportNumber='passportNumber', IPAddress='123.123.123.123'", "FAILED",
                "unknown", null, null, passportNumber);
    }

    @Test
    public void throwBadCredentialsExceptionIfDateOfBirthIsIllFormed() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";
        RemoteIPThreadLocal.set(ipAddress);
        final Authentication authentication = getAuthentication(passportNumber, "32 12 1997");

        try {
            visaAuthenticationProvider.authenticate(authentication);
            fail("should not reach this");
        } catch (AuthenticationException expectedAndIgnored) {
        }

        verify(loginAttemptRepository).logFailedAttempt(passportNumber, ipAddress);
        verify(auditService).audit("action='login', passportNumber='passportNumber', IPAddress='123.123.123.123'", "FAILED",
                "unknown", null, null, passportNumber);
    }

    private Authentication getAuthentication(String name, String credentials) {
        return new DummyAuthentication(name, credentials);
    }
}
