package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MiDAO {

    @RegisterRowMapper(VisaCountByStatusMapper.class)
    @SqlQuery(MiSQL.SELECT_VISA_COUNT_BY_STATUS)
    Collection<Tuple<VisaStatus, Integer>> selectVisaCountByStatus(@Bind("from") LocalDateTime from, @Bind("to") LocalDateTime to);

    @RegisterRowMapper(LoginStatusMapper.class)
    @SqlQuery(MiSQL.SELECT_USER_LOGIN_COUNT)
    Collection<Tuple<Boolean, Integer>> selectLoginCount(@Bind("from") LocalDateTime from, @Bind("to") LocalDateTime to);

}
