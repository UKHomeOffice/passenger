package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Optional.ofNullable;

public class EntryClearanceMapper implements RowMapper<EntryClearance> {

    @Override
    public EntryClearance map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return new EntryClearance(
                resultSet.getString("passport_number"),
                ofNullable(resultSet.getDate("start_date")).map(val -> val.toLocalDate()).orElse(null),
                ofNullable(resultSet.getDate("end_date")).map(val -> val.toLocalDate()).orElse(null),
                resultSet.getString("passport_nationality"),
                resultSet.getString("surname"),
                resultSet.getString("other_names"),
                resultSet.getString("tier_type"),
                ofNullable(resultSet.getDate("date_of_birth")).map(val -> val.toLocalDate()).orElse(null),
                resultSet.getString("vaf_number"),
                resultSet.getString("cas_number"),
                resultSet.getString("spx_number"),
                ofNullable(resultSet.getDate("visa_valid_to_date")).map(val -> val.toLocalDate()).orElse(null),
                resultSet.getString("conditions_1"),
                resultSet.getString("conditions_2")
        );
    }
}
