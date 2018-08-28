package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;


import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Travel;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Optional.ofNullable;

public class TravelMapper implements RowMapper<Travel> {

    @Override
    public Travel map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return new Travel(
                resultSet.getString("flightnumber"),
                resultSet.getString("departure_point"),
                ofNullable(resultSet.getTimestamp("departure_time")).map(val -> val.toLocalDateTime()).orElse(null),
                resultSet.getString("arrival_point"),
                ofNullable(resultSet.getTimestamp("arrival_time")).map(val -> val.toLocalDateTime()).orElse(null)
        );
    }
}
