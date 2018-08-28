package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;

public class FindAllVisaTypes implements HandleCallback<Collection<VisaType>, JdbiException> {

    @Override
    public Collection<VisaType> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(VisaTypeDAO.class).selectAll();
    }

}
