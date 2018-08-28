package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class SaveOrUpdateActionTest {

    @Test
    public void shouldSaveCrsRecord() {
        final CrsRecord crsRecord = crsRecord();

        final Handle mockHandle = mock(Handle.class);
        final CrsRecordDAO mockDAO = mock(CrsRecordDAO.class);

        when(mockHandle.attach(CrsRecordDAO.class)).thenReturn(mockDAO);
        when(mockDAO.getByPassportNumberAndDateOfBirth(crsRecord.getPassportNumber(), crsRecord.getDateOfBirth())).thenReturn(crsRecord);

        final SaveOrUpdateAction underTest = new SaveOrUpdateAction(crsRecord);
        underTest.useHandle(mockHandle);

        verify(mockDAO).update(crsRecord);
    }

    @Test
    public void shouldUpdateCrsRecord() {
        final CrsRecord crsRecord = crsRecord();

        final Handle mockHandle = mock(Handle.class);
        final CrsRecordDAO mockDAO = mock(CrsRecordDAO.class);

        when(mockHandle.attach(CrsRecordDAO.class)).thenReturn(mockDAO);
        when(mockDAO.getByPassportNumberAndDateOfBirth(crsRecord.getPassportNumber(), crsRecord.getDateOfBirth())).thenReturn(null);

        final SaveOrUpdateAction underTest = new SaveOrUpdateAction(crsRecord);
        underTest.useHandle(mockHandle);

        verify(mockDAO).save(crsRecord);
    }

    private CrsRecord crsRecord() {
        return CrsRecord.builder()
                .gwfRef("GWF046027237")
                .vafNo("1295789")
                .casNo("E4G8DF0F36F0V1")
                .familyName("Tim Spencer")
                .otherName("Bartholomew")
                .dateOfBirth(LocalDate.of(1996, 10, 22))
                .nationality("USA")
                .passportNumber("539701015")
                .mobileNumber("6316032284")
                .emailAddress("test1@test.com")
                .sponsorAddress("74 Academia Row, Haytown, Surrey, AB0 1CD")
                .build();
    }

}