package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;


import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

public class ParticipantMapper implements RowMapper<Participant> {
    @Override
    public Participant map(ResultSet resultSet, StatementContext ctx) throws SQLException {
        return new Participant(
                resultSet.getLong("id"),
                resultSet.getString("gwf_number"),
                resultSet.getString("vaf_number"),
                resultSet.getString("cas_number"),
                resultSet.getString("first_name"),
                resultSet.getString("middle_name"),
                resultSet.getString("surname"),
                ofNullable(resultSet.getDate("date_of_birth")).map(val -> val.toLocalDate()).orElse(null),
                resultSet.getString("nationality"),
                resultSet.getString("passport_number"),
                resultSet.getString("mobile_number"),
                resultSet.getString("email"),
                resultSet.getString("acl_address"),
                ofNullable(resultSet.getString("emails_sent")).map(val -> Arrays.asList(val.split(",")).stream().collect(Collectors.toSet())).orElse(emptySet()),
                resultSet.getString("updated_by"),
                ofNullable(resultSet.getTimestamp("created")).map(val -> val.toLocalDateTime()).orElse(null),
                ofNullable(resultSet.getTimestamp("last_updated")).map(val -> val.toLocalDateTime()).orElse(null)
        );
    }
}
