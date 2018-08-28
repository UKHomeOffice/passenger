package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class LogUnsuccessfulAttemptAction implements HandleConsumer<JdbiException> {

    private final String passportNumber;
    private final String ipAddress;

    public LogUnsuccessfulAttemptAction(final String passportNumber,
                                        final String ipAddress) {
        this.passportNumber = passportNumber;
        this.ipAddress = ipAddress;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(LoginAttemptDAO.class).logUnsuccessfulAttempt(passportNumber, ipAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogUnsuccessfulAttemptAction that = (LogUnsuccessfulAttemptAction) o;
        return Objects.equals(passportNumber, that.passportNumber) &&
                Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, ipAddress);
    }

}
