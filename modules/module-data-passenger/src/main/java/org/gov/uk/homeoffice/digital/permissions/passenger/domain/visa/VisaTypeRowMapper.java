package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class VisaTypeRowMapper implements RowMapper<VisaType> {

    @Override
    public VisaType map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new VisaType(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("notes"),
                rs.getString("description"),
                rs.getBoolean("enabled"),
                Optional.ofNullable(rs.getTimestamp("created")).map(Timestamp::toLocalDateTime).orElse(null)
        );
    }

}
