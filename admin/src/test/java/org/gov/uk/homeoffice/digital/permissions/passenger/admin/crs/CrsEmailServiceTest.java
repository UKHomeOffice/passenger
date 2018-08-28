package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.junit.Before;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrsEmailServiceTest {

    @Mock
    private NotifyService notifyService;

    private Boolean emailEnabled;

    @Mock
    private CrsRecordRepository crsRecordRepository;

    private CrsEmailService testObject;


    private final String baseUrl = "base-url";

    private final CrsRecord.CrsRecordBuilder crsRecordBuilder = CrsRecord.builder()
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

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void withEmailDisabledNoEmailIsSent() throws Exception {


        crsEmailServiceWithEmailDisabled().sendVisaEmail(aValidCrsRecord);

        verify(notifyService, never()).sendVisaGrantedEmail("emailAddress", "otherName", "familyName", "base-url");
    }

    @Test
    public void withEmailEnabledEmailIsSentForGrantedVisa() throws Exception {


        when(crsRecordRepository.getByPassportNumber(aValidCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aValidCrsRecord));

        crsEmailServiceWithEmailEnabled().sendVisaEmail(aValidCrsRecord);
        
        verify(notifyService).sendVisaGrantedEmail("emailAddress", "otherName", "familyName", baseUrl);
        verify(notifyService, never()).sendVisaRevokedEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void withEmailEnabledEmailIsNotSentForAlreadyNotifiedGrantedVisa() throws Exception {

        when(crsRecordRepository.getByPassportNumber(aValidCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aValidCrsRecordWithEmailSent));

        crsEmailServiceWithEmailEnabled().sendVisaEmail(aValidCrsRecord);

        verify(notifyService, never()).sendVisaGrantedEmail(anyString(), anyString(), anyString(), anyString());
        verify(notifyService, never()).sendVisaRevokedEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void withEmailEnabledEmailIsSentForRevokedVisa() throws Exception {

        when(crsRecordRepository.getByPassportNumber(aRevokedCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aRevokedCrsRecord));

        crsEmailServiceWithEmailEnabled().sendVisaEmail(aRevokedCrsRecord);

        verify(notifyService).sendVisaRevokedEmail("emailAddress", "otherName", "familyName", baseUrl);
        verify(notifyService, never()).sendVisaGrantedEmail(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void withEmailEnabledEmailIsNotSentForAlreadyNotifiedRevokedVisa() throws Exception {

        when(crsRecordRepository.getByPassportNumber(aRevokedCrsRecord.getPassportNumber()))
                .thenReturn(Optional.of(aRevokedCrsRecordWithEmailSent));

        crsEmailServiceWithEmailEnabled().sendVisaEmail(aRevokedCrsRecord);

        verify(notifyService, never()).sendVisaRevokedEmail(anyString(), anyString(), anyString(), anyString());
        verify(notifyService, never()).sendVisaGrantedEmail(anyString(), anyString(), anyString(), anyString());
    }

    private File aCrsFile() throws IOException {
        File file = File.createTempFile("crs_upload", null);
        file.deleteOnExit();
        return file;
    }

    private CrsEmailService crsEmailServiceWithEmailEnabled() {
        return service(true);
    }

    private CrsEmailService crsEmailServiceWithEmailDisabled() {
        return service(false);
    }

    private CrsEmailService service(boolean emailEnabled) {
        return new CrsEmailService(notifyService,
                emailEnabled,
                baseUrl,
                crsRecordRepository
        );
    }


}
