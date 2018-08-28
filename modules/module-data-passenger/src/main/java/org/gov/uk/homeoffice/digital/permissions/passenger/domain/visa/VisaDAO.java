package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;


import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface VisaDAO {

    @SqlUpdate(Query.INSERT_VISA)
    @GetGeneratedKeys
    Long save(@BindBean Visa visa);

    @SqlQuery(Query.SELECT_BY_PASSPORT_NUMBER)
    @RegisterRowMapper(VisaMapper.class)
    Visa getByPassportNumber(@Bind("passportNumber") String passportNumber);

    @SqlUpdate(Query.INSERT_ENDORSEMENT)
    void saveEndorsement(@Bind("value") String endorsement, @Bind("visaId") Long visaId);

    @SqlQuery(Query.SELECT_ENDORSEMENTS_FOR_VISA)
    List<String> getEndorsementsForVisa(@Bind("visaId") Long visaId);

    @SqlQuery(Query.SELECT_ID_FOR_PASSPORT_NUMBER)
    Long exists(@Bind("passportNumber") String passportNumber);

    @SqlUpdate(Query.UPDATE_VISA)
    void update(@BindBean Visa visa);

    @SqlUpdate(Query.DELETE_ENDORSEMENTS_FOR_VISA)
    void deleteEndorsementsForVisa(@Bind("visaId") long visaId);

}
