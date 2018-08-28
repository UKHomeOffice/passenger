package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt.LoginAttemptDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SelectAllLoginAttemptsBetween implements HandleCallback<List<LoginAttempt>, JdbiException> {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public SelectAllLoginAttemptsBetween(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public List<LoginAttempt> withHandle(Handle handle) throws JdbiException {
        return handle.attach(LoginAttemptDAO.class).allAttemptsBetween(from, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectAllLoginAttemptsBetween that = (SelectAllLoginAttemptsBetween) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

}
