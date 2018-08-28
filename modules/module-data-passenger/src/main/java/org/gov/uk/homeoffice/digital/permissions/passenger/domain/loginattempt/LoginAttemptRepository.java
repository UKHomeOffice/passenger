package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptRepository {

    void logFailedAttempt(String passportNumber, String ipAddress);

    void logSuccessfulAttempt(String passportNumber, String ipAddress);

    List<LoginAttempt> loginAttempts(String passportNumber, LocalDateTime since);

    List<LoginAttempt> allLoginAttemptsBetween(LocalDateTime from, LocalDateTime to);

}
