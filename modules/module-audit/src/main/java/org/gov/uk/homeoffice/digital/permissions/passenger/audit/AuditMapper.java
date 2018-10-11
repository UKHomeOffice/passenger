package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.gov.uk.homeoffice.digital.permissions.passenger.audit.domain.Audit;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AuditMapper implements RowMapper<Audit> {

    @Override
    public Audit map(ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Audit(
                resultSet.getLong("id"),
                resultSet.getString("user_name"),
                safeDateTime(resultSet.getTimestamp("date_time")),
                resultSet.getString("result"),
                resultSet.getString("content"),
                resultSet.getString("passenger_name"),
                resultSet.getString("passenger_email"),
                resultSet.getString("passenger_passport_no")
        );
    }

    private LocalDateTime safeDateTime(final Timestamp timestamp) {
        return (timestamp == null) ? null : timestamp.toLocalDateTime();
    }

}
