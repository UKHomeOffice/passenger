package org.gov.uk.homeoffice.digital.permissions.passenger.admin.crs;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrsUploadStatsServiceTest {

    @Mock
    CrsRecordRepository crsRecordRepository;

    @InjectMocks
    CrsUploadStatsService testObject;

    CrsParsedResult crsParsedResult;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testStatsWhenOneVisaIsInserted() {
        crsParsedResult = new CrsParsedResult(Arrays.asList(aValidCrsRecord), Collections.EMPTY_LIST);
        crsParsedResult.setUpdatedCrsRecords(Arrays.asList(aValidCrsRecord));

        when(crsRecordRepository.getByPassportNumberAndDateOfBirth(aRevokedCrsRecord.getPassportNumber(), aValidCrsRecord.getDateOfBirth())).thenReturn(Optional.empty());

        testObject.updateStats(crsParsedResult);

        assertThat(crsParsedResult.getNumberOfSuccessfullyCreatedRecords(), is(equalTo(1l)) );
        assertThat(crsParsedResult.getNumberOfSuccessfullyUpdatedRecords(), is(equalTo(0l)) );
        assertThat(crsParsedResult.getNumberOfRecordsInError(), is(equalTo(0l)) );

    }

    @Test
    public void testStatsWhenOneVisaIsUpdated() {
        aValidCrsRecord.setId(1l);//record already present
        crsParsedResult = new CrsParsedResult(Arrays.asList(aValidCrsRecord), Collections.EMPTY_LIST);
        crsParsedResult.setUpdatedCrsRecords(Arrays.asList(aValidCrsRecord));

        when(crsRecordRepository.getByPassportNumberAndDateOfBirth(aRevokedCrsRecord.getPassportNumber(), aValidCrsRecord.getDateOfBirth())).thenReturn(Optional.of(1l));

        testObject.updateStats(crsParsedResult);

        assertThat(crsParsedResult.getNumberOfSuccessfullyCreatedRecords(), is(equalTo(0l)) );
        assertThat(crsParsedResult.getNumberOfSuccessfullyUpdatedRecords(), is(equalTo(1l)) );
        assertThat(crsParsedResult.getNumberOfRecordsInError(), is(equalTo(0l)) );

    }

    @Test
    public void testStatsWhenNoVisaIsUpdatedDueToError() {
        crsParsedResult = new CrsParsedResult(Arrays.asList(aValidCrsRecord), Collections.EMPTY_LIST);

        when(crsRecordRepository.getByPassportNumberAndDateOfBirth(aRevokedCrsRecord.getPassportNumber(), aValidCrsRecord.getDateOfBirth())).thenReturn(Optional.empty());

        testObject.updateStats(crsParsedResult);

        assertThat(crsParsedResult.getNumberOfSuccessfullyCreatedRecords(), is(equalTo(0l)) );
        assertThat(crsParsedResult.getNumberOfSuccessfullyUpdatedRecords(), is(equalTo(0l)) );
        assertThat(crsParsedResult.getNumberOfRecordsInError(), is(equalTo(1l)) );

    }

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
}
