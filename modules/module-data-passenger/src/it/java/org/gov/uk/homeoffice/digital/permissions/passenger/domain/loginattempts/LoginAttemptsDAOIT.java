package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempts;

import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.PassengerDBITConfiguration;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PassengerDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class LoginAttemptsDAOIT {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Test
    public void insertFailedAttempt() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";

        dbi.useHandle(handle -> {
            final LoginAttemptDAO dao = handle.attach(LoginAttemptDAO.class);
            dao.logUnsuccessfulAttempt(passportNumber, ipAddress);
            List<LoginAttempt> attempts = dao.attemptsSince(passportNumber, LocalDateTime.now().minusMinutes(10));
            assertThat(attempts.size(), is(1));
            assertThat(attempts.get(0).passportNumber, is(passportNumber));
            assertThat(attempts.get(0).ipAddress, is(ipAddress));
            assertThat(attempts.get(0).success, is(false));
        });
    }

    @Test
    public void insertSuccessfulAttempt() {
        final String passportNumber = "passportNumber";
        final String ipAddress = "123.123.123.123";

        dbi.useHandle(handle -> {
            final LoginAttemptDAO dao = handle.attach(LoginAttemptDAO.class);
            dao.logSuccessfulAttempt(passportNumber, ipAddress);
            List<LoginAttempt> attempts = dao.attemptsSince(passportNumber, LocalDateTime.now().minusMinutes(10));
            assertThat(attempts.size(), is(1));
            assertThat(attempts.get(0).passportNumber, is(passportNumber));
            assertThat(attempts.get(0).ipAddress, is(ipAddress));
            assertThat(attempts.get(0).success, is(true));
        });
    }

    @Test
    public void getAllAttemptsBetweenDates() {
        dbi.useHandle(handle -> {
            final LoginAttemptDAO dao = handle.attach(LoginAttemptDAO.class);
            dao.logSuccessfulAttempt("passportNumber", "123.123.123.123");
            List<LoginAttempt> attempts = dao.allAttemptsBetween(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().plusMinutes(1));
            assertThat(attempts.size(), is(1));
        });
    }
}
