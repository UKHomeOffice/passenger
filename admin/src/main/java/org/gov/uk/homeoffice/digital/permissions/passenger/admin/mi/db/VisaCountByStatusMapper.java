package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisaCountByStatusMapper implements RowMapper<Tuple<VisaStatus, Integer>> {

    @Override
    public Tuple<VisaStatus, Integer> map(final ResultSet rs,
                                          final StatementContext ctx) throws SQLException {
        return Tuple.tpl(
                VisaStatus.valueOf(rs.getString("status")),
                rs.getInt(2)
        );
    }

}
