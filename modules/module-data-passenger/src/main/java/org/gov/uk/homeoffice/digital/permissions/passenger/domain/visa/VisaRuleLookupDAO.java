package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;
import java.util.Optional;

public interface VisaRuleLookupDAO {

    @SqlUpdate(VisaRuleLookupSQL.INSERT)
    void insert(@BindBean VisaRule visaRule);

    @SqlUpdate(VisaRuleLookupSQL.DELETE)
    void delete(@BindBean VisaRule visaRule);

    @SqlUpdate(VisaRuleLookupSQL.DELETE)
    void delete(@Bind("rule") String rule);

    @SqlQuery(VisaRuleLookupSQL.SELECT_BY_RULE)
    @RegisterRowMapper(VisaRuleRowMapper.class)
    Optional<VisaRule> selectByRule(@Bind("rule") String rule);

    @SqlQuery(VisaRuleLookupSQL.SELECT_ALL)
    @RegisterRowMapper(VisaRuleRowMapper.class)
    Collection<VisaRule> selectAll();

    @SqlQuery(VisaRuleLookupSQL.SELECT_ALL_ENABLED)
    @RegisterRowMapper(VisaRuleRowMapper.class)
    Collection<VisaRule> selectAllEnabled();

    @SqlQuery(VisaRuleLookupSQL.SELECT_ALL_OPTIONAL)
    @RegisterRowMapper(VisaRuleRowMapper.class)
    Collection<VisaRule> selectAllOptional();

    @SqlQuery(VisaRuleLookupSQL.SELECT_ALL_BY_VISA_TYPE)
    @RegisterRowMapper(VisaRuleRowMapper.class)
    Collection<VisaRule> selectAllByVisaType(@Bind("visaTypeId") Long visaTypeId);

}
