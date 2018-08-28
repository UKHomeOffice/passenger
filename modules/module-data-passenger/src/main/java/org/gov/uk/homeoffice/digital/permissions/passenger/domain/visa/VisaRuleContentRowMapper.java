package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.RuleType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisaRuleContentRowMapper implements RowMapper<VisaRuleContent> {

    @Override
    public VisaRuleContent map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new VisaRuleContent(
                rs.getLong("id"),
                rs.getString("rule"),
                rs.getString("content"),
                rs.getBoolean("enabled"),
                RuleType.valueOf(rs.getString("rule_type"))
        );
    }

}
