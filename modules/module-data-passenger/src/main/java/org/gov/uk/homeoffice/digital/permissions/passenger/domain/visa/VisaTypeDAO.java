package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;
import java.util.Optional;

public interface VisaTypeDAO {

    @SqlUpdate(VisaTypeSQL.INSERT)
    @GetGeneratedKeys
    Long insert(@BindBean VisaType visaType);

    @SqlUpdate(VisaTypeSQL.UPDATE)
    void update(@BindBean VisaType visaType);

    @SqlUpdate(VisaTypeSQL.DELETE)
    void delete(@Bind("id") Long id);

    @SqlQuery(VisaTypeSQL.SELECT_BY_ID)
    @RegisterRowMapper(VisaTypeRowMapper.class)
    Optional<VisaType> selectById(@Bind("id") Long id);

    @SqlQuery(VisaTypeSQL.SELECT_BY_NAME)
    @RegisterRowMapper(VisaTypeRowMapper.class)
    Optional<VisaType> selectByName(@Bind("name") String name);

    @SqlQuery(VisaTypeSQL.SELECT_ALL)
    @RegisterRowMapper(VisaTypeRowMapper.class)
    Collection<VisaType> selectAll();

}
