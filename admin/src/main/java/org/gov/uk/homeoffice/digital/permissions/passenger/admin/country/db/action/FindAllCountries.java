package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action;

import lombok.EqualsAndHashCode;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;

@EqualsAndHashCode
public class FindAllCountries implements HandleCallback<Collection<Country>, JdbiException> {

    @Override
    public Collection<Country> withHandle(final Handle handle) {
        return handle.attach(CountryDAO.class).findAll();
    }

}
