package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.system.storage.s3.S3StorageServiceBean;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord.CrsRecordBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleMatcher;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.CRSVisaRecordAdapter;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrsFileUploadServiceTest {

    @Mock
    private CrsFileParser crsFileParser;

    @Mock
    private S3StorageServiceBean storageService;

    @Mock
    private CrsRecordRepository crsRecordRepository;

    @Mock
    private NotifyService notifyService;

    @Mock
    private VisaRuleMatcher visaRuleMatcher;

    @Mock
    private CRSVisaRecordAdapter crsVisaRecordAdapter;

    private final String baseUrl = "base-url";

    private final CrsRecordBuilder crsRecordBuilder = CrsRecord.builder()
            .id(1234L)
            .passportNumber("passportNumber")
            .emailAddress("emailAddress")
            .otherName("otherName")
            .familyName("familyName");

    private final CrsRecord aValidCrsRecord = crsRecordBuilder
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

    @Test
    public void withEmailDisabledNoEmailIsSent() throws Exception {

        when(crsFileParser.parse(any(File.class)))
                .thenReturn(new CrsParsedResult(List.of(aValidCrsRecord), List.of()));

        when(crsVisaRecordAdapter.getVisaRecord(aValidCrsRecord))
                .thenReturn(aValidVisaRecord);
        when(visaRuleMatcher.hasVisaRule(eq(aValidVisaRecord), any(Consumer.class)))
                .thenReturn(true);

        crsFileUploadServiceWithEmailDisabled().process(aCrsFile(), "username");

        verify(crsRecordRepository).save(aValidCrsRecord);
        verify(notifyService, never()).sendVisaGrantedEmail("emailAddress", "otherName", "familyName", "base-url");
        verify(notifyService, never()).sendVisaRevokedEmail("emailAddress", "otherName", "familyName", "base-url");
    }

    @Test
    public void withEmailEnabledEmailIsSentForGrantedVisa() throws Exception {

        when(crsFileParser.parse(any(File.class)))
                .thenReturn(new CrsParsedResult(List.of(aValidCrsRecord), List.of()));

        when(crsVisaRecordAdapter.getVisaRecord(aValidCrsRecord))
                .thenReturn(aValidVisaRecord);
        when(visaRuleMatcher.hasVisaRule(eq(aValidVisaRecord), any(Consumer.class)))
                .thenReturn(true);

        when(crsRecordRepository.getByPassportNumber(aValidCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aValidCrsRecord));

        crsFileUploadServiceWithEmailEnabled().process(aCrsFile(), "username");

        verify(crsRecordRepository).save(aValidCrsRecord);
        verify(notifyService).sendVisaGrantedEmail("emailAddress", "otherName", "familyName", baseUrl);
        verify(notifyService, never()).sendVisaRevokedEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void withEmailEnabledEmailIsNotSentForAlreadyNotifiedGrantedVisa() throws Exception {

        when(crsFileParser.parse(any(File.class)))
                .thenReturn(new CrsParsedResult(List.of(aValidCrsRecord), List.of()));

        when(crsVisaRecordAdapter.getVisaRecord(aValidCrsRecord))
                .thenReturn(aValidVisaRecord);
        when(visaRuleMatcher.hasVisaRule(eq(aValidVisaRecord), any(Consumer.class)))
                .thenReturn(true);

        when(crsRecordRepository.getByPassportNumber(aValidCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aValidCrsRecordWithEmailSent));

        crsFileUploadServiceWithEmailEnabled().process(aCrsFile(), "username");

        verify(crsRecordRepository).save(aValidCrsRecord);
        verify(notifyService, never()).sendVisaGrantedEmail(anyString(), anyString(), anyString(), anyString());
        verify(notifyService, never()).sendVisaRevokedEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void withEmailEnabledEmailIsSentForRevokedVisa() throws Exception {

        when(crsFileParser.parse(any(File.class)))
                .thenReturn(new CrsParsedResult(List.of(aRevokedCrsRecord), List.of()));

        when(crsVisaRecordAdapter.getVisaRecord(aRevokedCrsRecord))
                .thenReturn(aRevokedVisaRecord);
        when(visaRuleMatcher.hasVisaRule(eq(aRevokedVisaRecord), any(Consumer.class)))
                .thenReturn(true);

        when(crsRecordRepository.getByPassportNumber(aRevokedCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aRevokedCrsRecord));

        crsFileUploadServiceWithEmailEnabled().process(aCrsFile(), "username");

        verify(crsRecordRepository).save(aRevokedCrsRecord);
        verify(notifyService).sendVisaRevokedEmail("emailAddress", "otherName", "familyName", baseUrl);
        verify(notifyService, never()).sendVisaGrantedEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void withEmailEnabledEmailIsNotSentForAlreadyNotifiedRevokedVisa() throws Exception {

        when(crsFileParser.parse(any(File.class)))
                .thenReturn(new CrsParsedResult(List.of(aRevokedCrsRecord), List.of()));

        when(crsVisaRecordAdapter.getVisaRecord(aRevokedCrsRecord))
                .thenReturn(aRevokedVisaRecord);
        when(visaRuleMatcher.hasVisaRule(eq(aRevokedVisaRecord), any(Consumer.class)))
                .thenReturn(true);

        when(crsRecordRepository.getByPassportNumber(aRevokedCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aRevokedCrsRecordWithEmailSent));

        crsFileUploadServiceWithEmailEnabled().process(aCrsFile(), "username");

        verify(crsRecordRepository).save(aRevokedCrsRecord);
        verify(notifyService, never()).sendVisaRevokedEmail(anyString(), anyString(), anyString(), anyString());
        verify(notifyService, never()).sendVisaGrantedEmail(anyString(), anyString(), anyString(), anyString());
    }

    private File aCrsFile() throws IOException {
        File file = File.createTempFile("crs_upload", null);
        file.deleteOnExit();
        return file;
    }

    private CrsFileUploadService crsFileUploadServiceWithEmailEnabled() {
        return service(true);
    }

    private CrsFileUploadService crsFileUploadServiceWithEmailDisabled() {
        return service(false);
    }

    private CrsFileUploadService service(boolean emailEnabled) {
        return new CrsFileUploadService(crsFileParser,
                storageService,
                crsRecordRepository,
                notifyService,
                visaRuleMatcher,
                crsVisaRecordAdapter,
                emailEnabled,
                baseUrl);
    }
}