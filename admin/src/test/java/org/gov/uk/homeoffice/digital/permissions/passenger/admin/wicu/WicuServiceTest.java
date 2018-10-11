package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.StorageService;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu.DailyWashContent.Type.NAME;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple3.tpl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WicuServiceTest {

    @Mock
    private WicuRepository wicuRepository;
    @Mock
    private DailyWashInclusionRangeCalculator rangeCalculator;
    @Mock
    private DailyWashFileCreator dailyWashFileCreator;
    @Mock
    private StorageService storageService;
    @Mock
    private AuditService auditService;
    @Mock
    private VisaRecordService mockVisaRecordService;

    private WicuService wicuService;

    private final Instant now = Instant.parse("2017-12-03T10:15:30.00Z");
    private final Clock clock = Clock.fixed(now, ZoneId.of("UTC"));

    private final UUID uuid = UUID.randomUUID();
    private final Supplier<UUID> fixedUuidSupplier = () -> uuid;

    @Before
    public void setUp() {
        wicuService = new WicuService(wicuRepository, rangeCalculator, dailyWashFileCreator, clock, fixedUuidSupplier,
                storageService, auditService, mockVisaRecordService);
    }

    @Test
    public void whenRecordsArePresentGenerateAndStoreFiles() throws Exception {
        LocalDate lowerLimit = LocalDate.parse("2017-12-01");
        LocalDate upperLimit = LocalDate.parse("2017-12-20");
        when(rangeCalculator.calculate(LocalDate.parse("2017-12-03"))).thenReturn(tpl(lowerLimit, upperLimit));

        List<VisaRecord> visaRecords = asList(
                new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("test"), Collections.emptyList()),
                new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("test"), Collections.emptyList())
        );
        when(mockVisaRecordService.getValidWithinRange(lowerLimit, upperLimit)).thenReturn(visaRecords);

        Optional<File> nameWash = aFileContaining("name1\nname2\n");
        Optional<File> docWash = aFileContaining("doc1\ndoc2\n");
        when(dailyWashFileCreator.createFiles(visaRecords, now)).thenReturn(tpl(docWash, nameWash, 2));

        wicuService.generate("user@example.com", "Users Name");

        verify(wicuRepository).save(new DailyWashCreation(uuid, now, 2, "user@example.com",
                docWash.get().getName(), nameWash.get().getName(), "Users Name"));

        verify(storageService).upload(docWash.get());
        verify(storageService).upload(nameWash.get());

        verify(auditService).audit("action='WICU daily wash file generation', " +
                String.format("documentCheckFileName='%s', ", docWash.get().getName()) +
                String.format("nameCheckFileName='%s', ", nameWash.get().getName()) +
                "numberOfRecords='2'", "SUCCESS", null, null, null
        );
        // it would be nice to consume and verify the input stream on the saveContent() call,
        // but the stream is already closed by the mockito call inside the try-with-resource
    }

    @Test
    public void whenRecordsAreNotPresentFilesAreNotGenerateAndNotStored() throws Exception {
        LocalDate lowerLimit = LocalDate.parse("2017-12-01");
        LocalDate upperLimit = LocalDate.parse("2017-12-20");
        when(rangeCalculator.calculate(LocalDate.parse("2017-12-03"))).thenReturn(tpl(lowerLimit, upperLimit));

        when(mockVisaRecordService.getValidWithinRange(lowerLimit, upperLimit)).thenReturn(emptyList());

        when(dailyWashFileCreator.createFiles(emptyList(), now)).thenReturn(tpl(Optional.empty(), Optional.empty(), 0));

        wicuService.generate("user@example.com", "Users Name");

        verify(wicuRepository).save(new DailyWashCreation(uuid, now, 0, "user@example.com",
                null, null, "Users Name"));

        verify(auditService).audit("action='WICU daily wash file generation', " +
                "documentCheckFileName='', " +
                "nameCheckFileName='', " +
                "numberOfRecords='0'", "SUCCESS", null, null, null
        );
    }

    @Test
    public void downloadFile() throws Exception {
        String csvContent = "123,content123\n4567,content4567";
        String username = "username";
        String fullName = "Full Name";
        String fileName = "name.csv";
        int numberOfRecords = 2;
        long creationId = 1L;

        DailyWashCreation creation = new DailyWashCreation(creationId, UUID.randomUUID(), Instant.now(),
                numberOfRecords,
                "creator@example.gov.uk",
                "doc.csv",
                fileName,
                "Creator Name");

        when(storageService.download(fileName))
                .thenReturn(new ByteArrayInputStream(csvContent.getBytes()));

        DailyWashContent content = wicuService.getFile(creation, NAME, username, fullName);



        assertThat(content, is(new DailyWashContent(fileName, NAME, csvContent)));

        verify(wicuRepository).log(new DailyWashDownload(creationId, NAME, username, fullName, now));

        verify(auditService).audit(String.format("action='WICU daily wash file download', fileName='%s', numberOfRecords='%d', id=[123,4567]",
                fileName, numberOfRecords), "SUCCESS", null, null, null
        );
    }

    private Optional<File> aFileContaining(String content) throws Exception {
        File tempFile = File.createTempFile("wash", ".csv");
        tempFile.deleteOnExit();

        try (PrintWriter out = new PrintWriter(tempFile)) {
            out.print(content);
        }

        return Optional.of(tempFile);
    }
}
