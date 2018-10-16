package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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

}