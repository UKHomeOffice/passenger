package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Collection;

public interface VisaRuleContentDAO {

    @RegisterRowMapper(VisaRuleContentRowMapper.class)
    @SqlQuery(VisaRuleContentSQL.SELECT_BY_RULE)
    Collection<VisaRuleContent> selectByRule(@Bind("rule") String rule);

}
