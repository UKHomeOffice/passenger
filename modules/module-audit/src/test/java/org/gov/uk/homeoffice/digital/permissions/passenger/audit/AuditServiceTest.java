package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import io.micrometer.core.instrument.MeterRegistry;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuditServiceTest {

    @Mock
    private Jdbi mockJdbi;

    @Mock
    private MeterRegistry mockMeterRegistry;

    @InjectMocks
    private AuditService underTest;

    @Test
    public void shouldFindByPassportNumber() {
        final String passportNumber = "1234556780";

        underTest.findByPassportNumber(passportNumber);

        verify(mockJdbi).withHandle(new FindByPassportNumber(passportNumber));
    }

    @Test
    public void shouldFindByPassengerEmailAddress() {
        final String emailAddress = "bruce.lee@jeetkune.do";

        underTest.findByPassengerEmail(emailAddress);

        verify(mockJdbi).withHandle(new FindByPassengerEmail(emailAddress));
    }

    @Test
    public void shouldFindByPassengerName() {
        final String name = "Bruce Lee";

        underTest.findByPassengerName(name);

        verify(mockJdbi).withHandle(new FindByPassengerName(name));
    }

    @Test
    public void shouldFindByAdministratorEmail() {
        final String emailAddress = "chuck.norris@karate.com";

        underTest.findByAdminEmail(emailAddress);

        verify(mockJdbi).withHandle(new FindByAdminEmail(emailAddress));
    }

}