package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.GetAttemptsSinceAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.LogSuccessfulAttemptAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.LogUnsuccessfulAttemptAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action.SelectAllLoginAttemptsBetween;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoginAttemptRepositoryBean implements LoginAttemptRepository {

    private final Jdbi jdbi;

    public LoginAttemptRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void logFailedAttempt(final String passportNumber, final String ipAddress) {
        jdbi.useTransaction(new LogUnsuccessfulAttemptAction(passportNumber, ipAddress));
    }

    public void logSuccessfulAttempt(final String passportNumber, final String ipAddress) {
        jdbi.useTransaction(new LogSuccessfulAttemptAction(passportNumber, ipAddress));
    }

    public List<LoginAttempt> loginAttempts(final String passportNumber, final LocalDateTime since) {
        return jdbi.inTransaction(new GetAttemptsSinceAction(passportNumber, since));
    }

    @Override
    public List<LoginAttempt> allLoginAttemptsBetween(LocalDateTime from, LocalDateTime to) {
        return jdbi.withHandle(new SelectAllLoginAttemptsBetween(from, to));
    }

}
