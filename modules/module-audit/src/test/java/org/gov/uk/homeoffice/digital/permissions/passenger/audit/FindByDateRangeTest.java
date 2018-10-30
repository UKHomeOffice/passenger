package org.gov.uk.homeoffice.digital.permissions.passenger.audit;

import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.*;

public class FindByDateRangeTest {

    @Test
    public void shouldFindByPassportNumber() {
        final LocalDate fromDate = LocalDate.of(2018, 10, 1);
        final LocalDate toDate = LocalDate.of(2018, 10, 31);

        final Handle mockHandle = mock(Handle.class);
        final AuditDAO mockAuditDAO = mock(AuditDAO.class);

        when(mockHandle.attach(AuditDAO.class)).thenReturn(mockAuditDAO);

        final FindByDateRange underTest = new FindByDateRange(fromDate, toDate);

        underTest.withHandle(mockHandle);

        verify(mockAuditDAO).selectByDateRange(LocalDateTime.of(fromDate, LocalTime.MIN),
                LocalDateTime.of(toDate, LocalTime.MAX));
    }
}