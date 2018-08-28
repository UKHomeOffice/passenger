package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptDAO {

    @SqlUpdate(Query.INSERT_UNSUCCESSFUL_ATTEMPT)
    void logUnsuccessfulAttempt(@Bind("passportNumber") String passportNumber, @Bind("ipAddress") String ipAddress);

    @SqlUpdate(Query.INSERT_SUCCESSFUL_ATTEMPT)
    void logSuccessfulAttempt(@Bind("passportNumber") String passportNumber, @Bind("ipAddress") String ipAddress);

    @SqlQuery(Query.SELECT_SINCE)
    @RegisterRowMapper(LoginAttemptMapper.class)
    List<LoginAttempt> attemptsSince(@Bind("passportNumber") String passportNumber, @Bind("since") LocalDateTime localDateTime);

    @SqlQuery(Query.SELECT_BETWEEN)
    @RegisterRowMapper(LoginAttemptMapper.class)
    List<LoginAttempt> allAttemptsBetween(@Bind("from") LocalDateTime from, @Bind("to") LocalDateTime to);

}
