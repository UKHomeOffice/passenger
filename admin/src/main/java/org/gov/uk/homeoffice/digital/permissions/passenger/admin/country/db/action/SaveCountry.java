package org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.action;

import lombok.EqualsAndHashCode;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.country.db.CountryDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Country;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

@EqualsAndHashCode
public class SaveCountry implements HandleCallback<String, JdbiException> {

    private Country country;

    public SaveCountry(final Country country) {
        this.country = country;
    }

    @Override
    public String withHandle(final Handle handle) throws JdbiException {
        final FindCountryById findById = new FindCountryById(country.getId());
        return findById.withHandle(handle)
                .map(c -> update(handle, c))
                .orElseGet(() -> insert(handle));
    }

    private String update(final Handle handle, final Country fromDb) {
        handle.attach(CountryDAO.class).update(country);
        return fromDb.getCountry().getCountry();
    }

    private String insert(final Handle handle) {
        handle.attach(CountryDAO.class).insert(country);
        return country.getCountry().getCountry();
    }

}
