package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.action;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db.MiDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.time.LocalDateTime;
import java.util.Collection;

@Value
@EqualsAndHashCode
public class FindVisaCountByStatus implements HandleCallback<Collection<Tuple<VisaStatus, Integer>>, JdbiException> {

    private LocalDateTime from;
    private LocalDateTime to;

    @Override
    public Collection<Tuple<VisaStatus, Integer>> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(MiDAO.class).selectVisaCountByStatus(from, to);
    }

}
