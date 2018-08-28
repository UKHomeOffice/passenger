package org.gov.uk.homeoffice.digital.permissions.passenger.domain.loginattempt;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.LoginAttempt;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Optional.ofNullable;

public class LoginAttemptMapper implements RowMapper<LoginAttempt> {

    @Override
    public LoginAttempt map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return new LoginAttempt(
                resultSet.getString("passport_number"),
                resultSet.getString("ip_address"),
                ofNullable(resultSet.getTimestamp("created")).map(val -> val.toLocalDateTime()).orElse(null),
                resultSet.getBoolean("success")
        );
    }
}
