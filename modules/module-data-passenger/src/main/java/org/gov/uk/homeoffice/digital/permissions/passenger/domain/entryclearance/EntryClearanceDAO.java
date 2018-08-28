package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;

public interface EntryClearanceDAO {

    @SqlUpdate(Query.INSERT_ENTRY_CLEARANCE)
    void save(@BindBean EntryClearance ec);

    @SqlUpdate(Query.UPDATE_ENTRY_CLEARANCE)
    void update(EntryClearance ec);

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER)
    @RegisterRowMapper(EntryClearanceMapper.class)
    EntryClearance getEntryClearanceByPassportNumber(@Bind("passportNumber") String passportNumber);

    EntryClearance getEntryClearanceByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth);

}
