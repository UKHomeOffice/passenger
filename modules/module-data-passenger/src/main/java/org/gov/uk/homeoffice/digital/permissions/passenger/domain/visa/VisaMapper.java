package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class VisaMapper implements RowMapper<Visa> {

    @Override
    public Visa map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return new Visa(
                resultSet.getLong("id"),
                resultSet.getString("passport_number"),
                ofNullable(resultSet.getDate("valid_from")).map(val -> val.toLocalDate()).orElse(null),
                ofNullable(resultSet.getDate("valid_to")).map(val -> val.toLocalDate()).orElse(null),
                resultSet.getString("spx_number"),
                Collections.emptyList(),
                Optional.ofNullable(resultSet.getString("status")).map(val -> VisaStatus.valueOf(val)).orElse(VisaStatus.VALID),
                resultSet.getString("reason")
        );
    }
}
