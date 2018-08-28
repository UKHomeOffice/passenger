package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GetAttemptsSinceAction implements HandleCallback<List<LoginAttempt>, JdbiException> {

    private final String passportNumber;
    private final LocalDateTime since;

    public GetAttemptsSinceAction(final String passportNumber,
                                  final LocalDateTime since) {
        this.passportNumber = passportNumber;
        this.since = since;
    }

    @Override
    public List<LoginAttempt> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(LoginAttemptDAO.class).attemptsSince(passportNumber, since);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetAttemptsSinceAction that = (GetAttemptsSinceAction) o;
        return Objects.equals(passportNumber, that.passportNumber) &&
                Objects.equals(since, that.since);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, since);
    }

}
