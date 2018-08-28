package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrsUploadStatsService {

    private final CrsRecordRepository crsRecordRepository;

    public CrsUploadStatsService(CrsRecordRepository crsRecordRepository) {
        this.crsRecordRepository = crsRecordRepository;
    }

    public void updateStats(CrsParsedResult parsedResult) {
        final List<Long> listOfExistingVisas = parsedResult.getCrsRecords()
                .stream()
                .map(r -> crsRecordRepository.getByPassportNumberAndDateOfBirth(r.getPassportNumber(), r.getDateOfBirth()))
                .filter(id -> id.isPresent())
                .map(id -> id.get())
                .collect(Collectors.toList());

        final long updatedCount = parsedResult.getUpdatedCrsRecords().stream().filter(r -> listOfExistingVisas.contains(r.getId())).count();
        final long createdCount = parsedResult.getUpdatedCrsRecords().size() - updatedCount;
        final long failedCount = parsedResult.getCrsRecords().size() - parsedResult.getUpdatedCrsRecords().size();

        parsedResult.setNumberOfSuccessfullyUpdatedRecords(updatedCount);
        parsedResult.setNumberOfSuccessfullyCreatedRecords(createdCount);
        parsedResult.setNumberOfRecordsInError(failedCount);

    }
}
