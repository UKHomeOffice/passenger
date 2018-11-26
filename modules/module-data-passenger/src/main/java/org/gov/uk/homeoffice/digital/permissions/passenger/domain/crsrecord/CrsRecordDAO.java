package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrsRecordDAO {

    @SqlUpdate(Query.INSERT_CRS_RECORD)
    @GetGeneratedKeys
    Long save(@BindBean CrsRecord crsRecord);

    @SqlQuery(Query.SELECT_BY_ID)
    @RegisterRowMapper(CrsRecordMapper.class)
    CrsRecord getById(@Bind("id") long id);

    @SqlQuery(Query.SELECT_ID_BY_ID)
    Long exists(@Bind("id") long crsRecordId);

    @SqlUpdate(Query.UPDATE_CRS_RECORD)
    void update(@BindBean CrsRecord crsRecord);

    @SqlQuery(Query.SELECT_ALL)
    @RegisterRowMapper(CrsRecordMapper.class)
    List<CrsRecord> getAll();

    @SqlUpdate(Query.DELETE_CRS_RECORD)
    void delete(@Bind("id") Long id);

    @SqlUpdate(Query.DELETE_CRS_RECORD_OLDER_THAN)
    void deleteOlderThan(@Bind("dateTime") LocalDateTime dateTime);

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER_AND_DATE_OF_BIRTH)
    @RegisterRowMapper(CrsRecordMapper.class)
    CrsRecord getByPassportNumberAndDateOfBirth(@Bind("passportNumber") String passportNumber, @Bind("dateOfBirth") LocalDate dateOfBirth);

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER)
    @RegisterRowMapper(CrsRecordMapper.class)
    CrsRecord getByPassportNumber(@Bind("passportNumber") String passportNumber);

    @SqlQuery(Query.SELECT_VALID_WITHIN_RANGE)
    @RegisterRowMapper(CrsRecordMapper.class)
    Collection<CrsRecord> getValidWithinRange(@Bind("lowerLimitIncluded") LocalDate lowerLimitIncluded, @Bind("upperLimitIncluded") LocalDate upperLimitIncluded);

}
