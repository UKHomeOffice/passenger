package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginStatusMapper implements RowMapper<Tuple<Boolean, Integer>> {

    @Override
    public Tuple<Boolean, Integer> map(final ResultSet rs,
                                       final StatementContext ctx) throws SQLException {
        return Tuple.tpl(
                rs.getBoolean("success"),
                rs.getInt(2)
        );
    }
}
