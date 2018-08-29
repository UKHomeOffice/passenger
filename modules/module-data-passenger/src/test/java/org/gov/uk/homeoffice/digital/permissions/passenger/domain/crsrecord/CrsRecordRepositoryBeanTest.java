package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.CrsRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action.SaveOrUpdateAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action.SelectByIdAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action.SelectByPassportNumber;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action.SelectValidWithinRange;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CrsRecordRepositoryBeanTest {

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private CrsRecordRepositoryBean underTest;

    @Test
    public void saveCrsRecord() {
        final CrsRecord crsRecord = CrsRecord.builder()
                .id(1L)
                .passportNumber("ab")
                .build();

        underTest.save(crsRecord);
        verify(mockJdbi).useTransaction(new SaveOrUpdateAction(crsRecord));
    }

    @Test
    public void loadCrsRecord() {
        final long id = 1L;

        underTest.getById(id);
        verify(mockJdbi).withHandle(new SelectByIdAction(id));
    }

    @Test
    public void getValidWithinRange() {
        final LocalDate lowerDateInc = LocalDate.now().minusDays(1);
        final LocalDate upperDateInc = LocalDate.now();

        underTest.getValidWithinRange(lowerDateInc, upperDateInc);

        verify(mockJdbi).withHandle(new SelectValidWithinRange(lowerDateInc, upperDateInc));
    }

    @Test
    public void selectByPassportNumberAction() {

        final String passportNumber = "passportNumber";

        underTest.getByPassportNumber(passportNumber);

        verify(mockJdbi).withHandle(new SelectByPassportNumber(passportNumber));
    }
}
