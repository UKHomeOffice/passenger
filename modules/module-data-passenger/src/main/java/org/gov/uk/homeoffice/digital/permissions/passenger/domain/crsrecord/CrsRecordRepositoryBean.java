package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action.*;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class CrsRecordRepositoryBean implements CrsRecordRepository {

    private final Jdbi jdbi;

    public CrsRecordRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void save(final CrsRecord crsRecord) {
        jdbi.useTransaction(new SaveOrUpdateAction(crsRecord));
    }

    public Optional<CrsRecord> getById(long id) {
        return jdbi.withHandle(new SelectByIdAction(id));
    }

    public Optional<List<CrsRecord>> getAll() {
        return jdbi.withHandle(new SelectAllAction());
    }

    public void deleteById(long id) {
        jdbi.useTransaction(new DeleteCrsRecordByIdAction(id));
    }

    @Override
    public Optional<Long> getByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth) {
        return jdbi.withHandle(new SelectByPassportNumberAndDOBAction(passportNumber, dateOfBirth)).map(crsRecord -> crsRecord.getId());
    }

    @Override
    public Optional<CrsRecord> getByPassportNumber(String passportNumber) {
        return jdbi.withHandle(new SelectByPassportNumber(passportNumber));
    }

    @Override
    public Collection<CrsRecord> getValidWithinRange(final LocalDate lowerLimit, final LocalDate upperLimit) {
        return jdbi.withHandle(new SelectValidWithinRange(lowerLimit, upperLimit));
    }

}
