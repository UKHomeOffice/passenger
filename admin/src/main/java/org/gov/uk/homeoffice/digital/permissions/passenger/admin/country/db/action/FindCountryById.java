package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action;

import lombok.EqualsAndHashCode;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Optional;

@EqualsAndHashCode
public class FindCountryById implements HandleCallback<Optional<Country>, JdbiException> {

    private String id;

    public FindCountryById(final String id) {
         this.id = id;
    }

    @Override
    public Optional<Country> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(CountryDAO.class).findByCountryCode(id);
    }
}
