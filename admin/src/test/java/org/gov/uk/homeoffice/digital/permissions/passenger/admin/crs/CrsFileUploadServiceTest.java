package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.StorageService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord.CrsRecordBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleMatcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CRSVisaRecordAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CrsFileUploadServiceTest {

    public static final String TEST_FILE = "testFile";

    @Mock
    private CrsFileParser crsFileParser;

    @Mock
    private StorageService storageService;

    @Mock
    private CrsRecordRepository crsRecordRepository;

    @Mock
    private VisaRuleMatcher visaRuleMatcher;

    @Mock
    private CRSVisaRecordAdapter crsVisaRecordAdapter;

    @Mock
    private CrsEmailService crsEmailService;

    @Mock
    private File file;

    @InjectMocks
    private CrsFileUploadService testObject;


    private final String baseUrl = "base-url";

    private final CrsRecordBuilder crsRecordBuilder = CrsRecord.builder()
            .id(1234L)
            .passportNumber("passportNumber")
            .emailAddress("emailAddress")
            .otherName("otherName")
            .familyName("familyName");

    private final CrsRecord aValidCrsRecord = crsRecordBuilder
            .id(1L)
            .emailAddress("emailAddress")
            .passportNumber("passportNumber")
            .dateOfBirth(LocalDate.of(1999, 1, 1))
            .nationality("nationality")
            .status(VisaStatus.VALID)
            .emailsSent(Set.of())
            .build();

    private final CrsRecord aValidCrsRecordWithEmailSent = crsRecordBuilder
            .status(VisaStatus.VALID)
            .emailsSent(Set.of("GRANTED"))
            .build();

    private final CrsRecord aRevokedCrsRecord = crsRecordBuilder
            .status(VisaStatus.REVOKED)
            .emailsSent(Set.of())
            .build();

    private final CrsRecord aRevokedCrsRecordWithEmailSent = crsRecordBuilder
            .status(VisaStatus.REVOKED)
            .emailsSent(Set.of("REVOKED"))
            .build();

    private final VisaRecord aValidVisaRecord = new VisaRecord(
            VisaStatus.VALID,
            VisaType.createVisaType("visa-type"),
            Collections.emptySet());

    private final VisaRecord aRevokedVisaRecord = new VisaRecord(
            VisaStatus.REVOKED,
            VisaType.createVisaType("visa-type"),
            Collections.emptySet());

    @Before
    public void setUp() throws Exception {
        when(file.getName()).thenReturn(TEST_FILE);
    }

    @Test
    public void testCrsFileUploadForAValidFile() throws IOException {

        CrsParsedResult crsParsedResult = new CrsParsedResult(Arrays.asList(aValidCrsRecord), new ArrayList<>());

        when(crsFileParser.parse(file)).thenReturn(crsParsedResult);
        when(crsVisaRecordAdapter.getVisaRecord(aValidCrsRecord)).thenReturn(aValidVisaRecord);
        when(visaRuleMatcher.hasVisaRule(any(), any())).thenReturn(true);

        testObject.process(file, "testUserName");

        verify(crsVisaRecordAdapter, times(1)).getVisaRecord(aValidCrsRecord);
        verify(crsRecordRepository, times(1)).save(aValidCrsRecord);
        verify(crsEmailService, times(1)).sendVisaEmail(aValidCrsRecord);

    }

    @Test
    public void testCrsFileUploadForARevokedVisa() throws IOException {

        CrsParsedResult crsParsedResult = new CrsParsedResult(Arrays.asList(aRevokedCrsRecord), new ArrayList<>());

        when(crsFileParser.parse(file)).thenReturn(crsParsedResult);
        when(visaRuleMatcher.hasVisaRule(any(), any())).thenReturn(true);

        testObject.process(file, "testUserName");

        verify(crsVisaRecordAdapter, times(1)).getVisaRecord(aRevokedCrsRecord);
        verify(crsRecordRepository, times(1)).save(aRevokedCrsRecord);
        verify(crsEmailService, times(1)).sendVisaEmail(aRevokedCrsRecord);

    }

    @Test
    public void testCrsFileUploadForanInvalidVisa() throws IOException {

        CrsParsedResult crsParsedResult = new CrsParsedResult(Arrays.asList(aRevokedCrsRecord), new ArrayList<>());

        when(crsFileParser.parse(file)).thenReturn(crsParsedResult);
        when(visaRuleMatcher.hasVisaRule(any(), any())).thenReturn(false); //invalid visa

        testObject.process(file, "testUserName");

        verify(crsVisaRecordAdapter, times(1)).getVisaRecord(aRevokedCrsRecord);
        verifyNoMoreInteractions(crsRecordRepository);
        verifyNoMoreInteractions(crsEmailService);

    }

}