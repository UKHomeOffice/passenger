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

    @SqlQuery(Query.QUERY_COUNT_IN_RANGE)
    Long countInDateRange(@Bind("fromDate") String fromDate, @Bind("toDate") String toDate);

    @RegisterRowMapper(AuditMapper.class)
    @SqlQuery(Query.QUERY_SELECT_IN_RANGE)
    Collection<Audit> selectInRange(@Bind("fromDate") String fromDate, @Bind("toDate") String toDate);

    @RegisterRowMapper(AuditMapper.class)
    @SqlQuery(Query.QUERY_SELECT_IN_RANGE_FOR_USER)
    Collection<Audit> selectInRangeForUser(@Bind("fromDate") String fromDate, @Bind("toDate") String toDate, @Bind("userName") String userName);

    @RegisterRowMapper(AuditMapper.class)
    @SqlQuery(Query.QUERY_SELECT_IN_RANGE_FOR_TEAM)
    Collection<Audit> selectInRangeForTeam(@Bind("fromDate") String fromDate, @Bind("toDate") String toDate, @Bind("teamName") String teamname);

    @RegisterRowMapper(AuditMapper.class)
    @SqlQuery(Query.QUERY_SELECT_IN_RANGE_FOR_USER_AND_TEAM)
    Collection<Audit> selectInRangeForUserAndTeam(@Bind("fromDate") String fromDate, @Bind("toDate") String toDate, @Bind("userName") String userName, @Bind("teamName") String teamname);

}