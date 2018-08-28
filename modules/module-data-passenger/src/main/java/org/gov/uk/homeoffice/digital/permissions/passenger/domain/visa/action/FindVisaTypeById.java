package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class FindVisaTypeById implements HandleCallback<Optional<VisaType>, JdbiException> {

    private Long id;

    public FindVisaTypeById(final Long id) {
        this.id = id;
    }

    @Override
    public Optional<VisaType> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(VisaTypeDAO.class).selectById(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindVisaTypeById that = (FindVisaTypeById) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
