package org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.repository.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.mi.db.MiDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class FindVisaCountByStatusTest {

    @Test
    public void shouldFindLoginStatusCount() {
        final LocalDateTime from = LocalDateTime.now();
        final LocalDateTime to = LocalDateTime.now();

        final FindVisaCountByStatus underTest = new FindVisaCountByStatus(from, to);

        final Handle mockHandle = mock(Handle.class);
        final MiDAO mockDao = mock(MiDAO.class);

        when(mockHandle.attach(MiDAO.class)).thenReturn(mockDao);

        underTest.withHandle(mockHandle);

        verify(mockHandle).attach(MiDAO.class);
        verify(mockDao).selectVisaCountByStatus(from, to);
    }

}