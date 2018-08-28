package org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.EntryClearance;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action.SaveOrUpdateAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action.SelectByPassportNumberAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.entryclearance.action.SelectByPassportNumberAndDOBAction;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EntryClearanceRepositoryBeanTest {

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private EntryClearanceRepositoryBean entryClearanceRepositoryBean;

    @Test
    public void testGetByPassportNumber() {
        String passportNumber = "AB123456YZ";
        entryClearanceRepositoryBean.getEntryClearanceByPassportNumber(passportNumber);
        verify(mockJdbi).withHandle(new SelectByPassportNumberAction(passportNumber));
    }

    @Test
    public void shouldCreateOrUpdate() {
        EntryClearance mockEntryClearance = mock(EntryClearance.class);
        entryClearanceRepositoryBean.createOrUpdate(mockEntryClearance);
        verify(mockJdbi).useTransaction(new SaveOrUpdateAction(mockEntryClearance));
    }

    @Test
    public void shouldGetByPassportNumberAndDateOfBirth() {
        final String passportNumber = "AB123456YZ";
        final LocalDate dateOfBirth = LocalDate.of(1940, 6, 14);
        entryClearanceRepositoryBean.getByPassportNumberAndDateOfBirth(passportNumber, dateOfBirth);
        verify(mockJdbi).withHandle(new SelectByPassportNumberAndDOBAction(passportNumber, dateOfBirth));
    }

}
