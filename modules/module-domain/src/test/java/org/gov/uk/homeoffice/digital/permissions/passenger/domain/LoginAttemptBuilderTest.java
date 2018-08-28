package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginAttemptBuilderTest {

    @Test
    public void shouldBuildLoginAttempt() {
        LocalDateTime time = LocalDateTime.now();

        LoginAttempt loginAttempt = new LoginAttemptBuilder()
                .setPassportNumber("passportNumber")
                .setIpAddress("ipAddress")
                .setSuccess(true)
                .setTime(time)
                .createLoginAttempt();

        assertThat(loginAttempt.passportNumber, is("passportNumber"));
        assertThat(loginAttempt.ipAddress, is("ipAddress"));
        assertThat(loginAttempt.success, is(true));
        assertThat(loginAttempt.time, is(time));
    }

}