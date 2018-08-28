package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public interface ParticipantDAO {

    @SqlUpdate(Query.INSERT_PARTICIPANT)
    void save(@BindBean Participant participant);

    @SqlQuery(Query.SELECT_BY_ID)
    @RegisterRowMapper(ParticipantMapper.class)
    Participant getById(@Bind("id") long id);

    @SqlQuery(Query.SELECT_BY_ID_BY_ID)
    Long exists(@Bind("id") long participantId);

    @SqlUpdate(Query.UPDATE_PARTICIPANT)
    void update(@BindBean Participant participant);

    @SqlQuery(Query.SELECT_ALL)
    @RegisterRowMapper(ParticipantMapper.class)
    List<Participant> getAll();

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER)
    @RegisterRowMapper(ParticipantMapper.class)
    Participant getByPassportNumber(@Bind("passportNumber") String passportNumber);

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER_AND_DATE_OF_BIRTH)
    @RegisterRowMapper(ParticipantMapper.class)
    Participant getByPassportNumberAndDateOfBirth(@Bind("passportNumber") String passportNumber, @Bind("dateOfBirth") LocalDate dateOfBirth);

    @SqlQuery(Query.SELECT_BY_VISA_VALID_FROM_RANGE)
    @RegisterRowMapper(ParticipantMapper.class)
    List<Participant> getByVisaValidFrom(@Bind("lowerLimitIncluded") LocalDate lowerLimitIncluded,
                                             @Bind("upperLimitIncluded") LocalDate upperLimitIncluded);

    @SqlUpdate(Query.UPDATE_EMAILS_SENT)
    void setEmailsSent(@BindBean Participant participant);

    @SqlUpdate(Query.DELETE_PARTICIPANT)
    void delete(@Bind("passportNumber") String passportNumber);
}
