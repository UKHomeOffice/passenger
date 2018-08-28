package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SelectAllAction implements HandleCallback<Optional<List<CrsRecord>>, JdbiException> {

    @Override
    public Optional<List<CrsRecord>> withHandle(Handle handle) throws JdbiException {
        return Optional.ofNullable(handle.attach(CrsRecordDAO.class).getAll());
    }

}
