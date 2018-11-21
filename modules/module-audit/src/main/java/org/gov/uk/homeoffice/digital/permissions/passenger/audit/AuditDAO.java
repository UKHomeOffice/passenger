package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Collection;

public interface AuditDAO {

    @SqlUpdate(Query.QUERY_INSERT_AUDIT)
    @GetGeneratedKeys
    Long insert(@BindBean Audit auditRow);

    @SqlQuery(Query.QUERY_COUNT)
    Long count();

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER)
    @RegisterRowMapper(AuditMapper.class)
    Collection<Audit> selectByPassportNumber(@Bind("passportNumber") String passportNumber);

    @SqlQuery(Query.SELECT_BY_PASSENGER_EMAIL)
    @RegisterRowMapper(AuditMapper.class)
    Collection<Audit> selectByPassengerEmail(@Bind("emailAddress") String emailAddress);

    @SqlQuery(Query.SELECT_BY_PASSENGER_NAME)
    @RegisterRowMapper(AuditMapper.class)
    Collection<Audit> selectByPassengerName(@Bind("name") String passengerName);

    @SqlQuery(Query.SELECT_BY_ADMIN_EMAIL)
    @RegisterRowMapper(AuditMapper.class)
    Collection<Audit> selectByAdministratorEmail(@Bind("emailAddress") String emailAddress);

    @SqlQuery(Query.SELECT_BY_QUERY)
    @RegisterRowMapper(AuditMapper.class)
    Collection<Audit> selectByQuery(
            @Bind("adminEmailAddress") String adminEmailAddress,
            @Bind("passportNumber") String passportNumber,
            @Bind("name") String passengerName,
            @Bind("emailAddress") String passengerEmailAddress,
            @Bind("from") LocalDateTime from,
            @Bind("to") LocalDateTime to);

    @SqlQuery(Query.SELECT_BY_DATE_RANGE)
    @RegisterRowMapper(AuditMapper.class)
    Collection<Audit> selectByDateRange(@Bind("from") LocalDateTime from, @Bind("to") LocalDateTime to);

}
