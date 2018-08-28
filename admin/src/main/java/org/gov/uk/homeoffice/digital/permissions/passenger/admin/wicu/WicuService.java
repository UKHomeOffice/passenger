package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.apache.commons.io.IOUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.StorageService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class WicuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WicuService.class);

    private final WicuRepository wicuRepository;
    private final Clock clock;
    private final DailyWashInclusionRangeCalculator rangeCalculator;
    private final DailyWashFileCreator dailyWashFileCreator;
    private final Supplier<UUID> uuidSupplier;
    private final StorageService storageService;
    private final AuditService auditService;
    private VisaRecordService visaRecordService;

    @Autowired
    public WicuService(WicuRepository wicuRepository,
                       DailyWashInclusionRangeCalculator rangeCalculator,
                       DailyWashFileCreator dailyWashFileCreator,
                       Clock clock, Supplier<UUID> uuidSupplier,
                       final StorageService storageService,
                       @Qualifier("audit.admin") AuditService auditService,
                       final VisaRecordService visaRecordService) {
        this.wicuRepository = wicuRepository;
        this.rangeCalculator = rangeCalculator;
        this.dailyWashFileCreator = dailyWashFileCreator;
        this.clock = clock;
        this.uuidSupplier = uuidSupplier;
        this.storageService = storageService;
        this.auditService = auditService;
        this.visaRecordService = visaRecordService;
    }

    public List<DailyWashView> files(int days) {
        return wicuRepository.getLatest(days);
    }

    public void generate(String username, String fullName) {
        Instant now = Instant.now(clock);
        LocalDate date = LocalDate.from(now.atZone(ZoneId.systemDefault()));
        Tuple<LocalDate, LocalDate> range = rangeCalculator.calculate(date);

        Collection<VisaRecord> visaRecords = visaRecordService.getValidWithinRange(range.get_1(), range.get_2());

        Tuple3<Optional<File>, Optional<File>, Integer> files = dailyWashFileCreator.createFiles(visaRecords, now);

        UUID creationUuid = uuidSupplier.get();
        wicuRepository.save(new DailyWashCreation(
                creationUuid,
                now,
                files._3,
                username,
                files._1.map(File::getName).orElse(null),
                files._2.map(File::getName).orElse(null),
                fullName)
        );

        if (files._3 > 0) {
            try {
                storageService.upload(files._1.get());
                storageService.upload(files._2.get());
            }
            catch (final IOException e) {
                LOGGER.error("Unable to upload daily wash files to S3.", e);
                throw new WicuException(e);
            }
        }

        auditService.audit(String.format("action='WICU daily wash file generation', documentCheckFileName='%s', nameCheckFileName='%s', numberOfRecords='%s'",
                files._1.map(File::getName).orElse(""),
                files._2.map(File::getName).orElse(""),
                files._3), "SUCCESS", username
        );
    }

    public DailyWashContent getFile(DailyWashCreation creation, DailyWashContent.Type type, String username, final String fullName) throws IOException {
        final String fileName = getFilenameByType(creation, type);
        final InputStream contentStream = storageService.download(fileName);
        final DailyWashDownload download = new DailyWashDownload(creation.id, type, username, fullName, Instant.now(clock));
        wicuRepository.log(download);
        String content = IOUtils.toString(contentStream, StandardCharsets.UTF_8);
        audit(username, content, fileName);
        return new DailyWashContent(fileName, type, content);
    }

    private String getFilenameByType(DailyWashCreation dailyWashCreation, DailyWashContent.Type type) {
        if (type == DailyWashContent.Type.DOC) {
            return dailyWashCreation.documentCheckFilename;
        } else if (type == DailyWashContent.Type.NAME) {
            return dailyWashCreation.nameCheckFilename;
        }
        return null;
    }

    public DailyWashCreation get(UUID id) {
        return wicuRepository.get(id);
    }

    private void audit(String username, String content, String fileName) {
        try{
            List<String> listIds = Arrays.asList(content.split("\n"))
                    .stream()
                    .map(row -> row.split(",")[0])
                    .collect(Collectors.toList());
            auditService.audit(String.format("action='WICU daily wash file download', fileName='%s', numberOfRecords='%s', id=[%s]",
                    fileName,
                    listIds.size(),
                    listIds.stream().collect(Collectors.joining(","))), "SUCCESS", username
            );
        } catch (Exception e){
            LOGGER.debug(e.getMessage());
        }
    }
}