package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Collection;
import java.util.Optional;

public interface CountryDAO {

    @SqlUpdate(CountrySQL.INSERT)
    @RegisterArgumentFactory(CountryArgumentFactory.class)
    void insert(@BindBean Country country);

    @SqlUpdate(CountrySQL.UPDATE)
    @RegisterArgumentFactory(CountryArgumentFactory.class)
    void update(@BindBean Country country);

    @SqlQuery(CountrySQL.SELECT_ALL)
    @RegisterRowMapper(CountryRowMapper.class)
    Collection<Country> findAll();

    @SqlQuery(CountrySQL.SELECT_BY_ID)
    @RegisterRowMapper(CountryRowMapper.class)
    Optional<Country> findByCountryCode(@Bind("id") String id);

}
