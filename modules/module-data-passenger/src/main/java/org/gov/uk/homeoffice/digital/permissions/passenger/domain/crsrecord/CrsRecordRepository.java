package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrsRecordRepository {

    void save(CrsRecord crsRecord);

    Optional<CrsRecord> getById(long id);

    Optional<List<CrsRecord>> getAll();

    void deleteById(long id);

    void deleteOlderThan(LocalDateTime dateTime);

    Optional<Long> getByPassportNumberAndDateOfBirth(String passportNumber, LocalDate dateOfBirth);

    Optional<CrsRecord> getByPassportNumber(String passportNumber);

    Collection<CrsRecord> getValidWithinRange(LocalDate lowerLimit, LocalDate upperLimit);

}
