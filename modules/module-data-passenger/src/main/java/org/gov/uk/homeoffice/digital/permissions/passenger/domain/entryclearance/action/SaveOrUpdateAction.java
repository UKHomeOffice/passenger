package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.EntryClearanceDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class SaveOrUpdateAction implements HandleConsumer<JdbiException> {

    private final EntryClearance entryClearance;

    public SaveOrUpdateAction(EntryClearance entryClearance) {
        this.entryClearance = entryClearance;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        final EntryClearanceDAO dao = handle.attach(EntryClearanceDAO.class);
        if (dao.getEntryClearanceByPassportNumber(entryClearance.getPassportNumber()) == null){
            dao.save(entryClearance);
        } else {
            dao.update(entryClearance);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveOrUpdateAction that = (SaveOrUpdateAction) o;
        return Objects.equals(entryClearance, that.entryClearance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryClearance);
    }

}
