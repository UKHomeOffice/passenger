package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;

public class CountryRowMapper implements RowMapper<Country> {

    @Override
    public Country map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Country(
                new Locale("", rs.getString("iso_country_code")),
                rs.getBoolean("enabled"),
                rs.getString("export_country"),
                Optional.ofNullable(rs.getTimestamp("created")).map(Timestamp::toLocalDateTime).orElse(null),
                Optional.ofNullable(rs.getTimestamp("updated")).map(Timestamp::toLocalDateTime).orElse(null)
        );
    }

}
