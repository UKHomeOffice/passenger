package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Catcher.convert;

public class SaveOrUpdateAction implements HandleConsumer<JdbiException> {

    private final CrsRecord crsRecord;

    public SaveOrUpdateAction(final CrsRecord crsRecord) {
        this.crsRecord = crsRecord;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        final RuntimeException exception = new RuntimeException("CrsRecord " + crsRecord + " could not be saved");
        final CrsRecordDAO dao = handle.attach(CrsRecordDAO.class);
        Boolean result = ofNullable(dao.getByPassportNumberAndDateOfBirth(crsRecord.getPassportNumber(), crsRecord.getDateOfBirth()))
                .map(storedCrsRecord -> {
                    crsRecord.setId(storedCrsRecord.getId());
                    convert(() -> dao.update(crsRecord), exception);
                    return true;
                }).orElseGet(() -> {
                    convert(() -> crsRecord.setId(dao.save(crsRecord)), exception);
                    crsRecord.setCreated(true);
                    return true;
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveOrUpdateAction that = (SaveOrUpdateAction) o;
        return Objects.equals(crsRecord, that.crsRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crsRecord);
    }

}
