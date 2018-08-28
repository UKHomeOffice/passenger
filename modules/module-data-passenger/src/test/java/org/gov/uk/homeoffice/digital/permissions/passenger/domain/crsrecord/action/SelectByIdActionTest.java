package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SelectByIdActionTest {

    @Test
    public void shouldSelectById() {
        final Long id = 121L;

        final Handle mockHandle = mock(Handle.class);
        final CrsRecordDAO mockDAO = mock(CrsRecordDAO.class);

        when(mockHandle.attach(CrsRecordDAO.class)).thenReturn(mockDAO);

        final SelectByIdAction underTest = new SelectByIdAction(id);
        underTest.withHandle(mockHandle);

        verify(mockDAO).getById(id);
    }

}