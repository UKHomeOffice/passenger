package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisaRuleRowMapper implements RowMapper<VisaRule> {

    @Override
    public VisaRule map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new VisaRule(
                rs.getString("rule"),
                rs.getBoolean("enabled")
        );
    }

}
