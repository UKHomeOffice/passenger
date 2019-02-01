package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class FindVisaTypeByName implements HandleCallback<Optional<VisaType>, JdbiException> {

    private String name;

    public FindVisaTypeByName(final String name) {
        this.name = name;
    }

    @Override
    public Optional<VisaType> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(VisaTypeDAO.class).selectByName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindVisaTypeByName that = (FindVisaTypeByName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
