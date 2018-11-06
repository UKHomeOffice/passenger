package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.action.FindLoginStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.action.FindVisaCountByStatus;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MiRepositoryBeanTest {

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private MiRepositoryBean underTest;

    @Test
    public void shouldCountVisaByStatus() {
        final LocalDateTime from = LocalDateTime.now();
        final LocalDateTime to = LocalDateTime.now();

        underTest.countVisaByStatus(from, to);

        verify(mockJdbi).withHandle(new FindVisaCountByStatus(from, to));
    }

    @Test
    public void shouldCountSuccessfulLogin() {
        final LocalDateTime from = LocalDateTime.now();
        final LocalDateTime to = LocalDateTime.now();

        underTest.countSuccessfulLogin(from, to);

        verify(mockJdbi).withHandle(new FindLoginStatus(from, to));
    }

}