package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import java.time.LocalDateTime;

public class LoginAttemptBuilder {
    private String passportNumber;
    private String ipAddress;
    private LocalDateTime time;
    private boolean success;

    public LoginAttemptBuilder setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public LoginAttemptBuilder setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public LoginAttemptBuilder setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public LoginAttemptBuilder setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public LoginAttempt createLoginAttempt() {
        return new LoginAttempt(passportNumber, ipAddress, time, success);
    }
}