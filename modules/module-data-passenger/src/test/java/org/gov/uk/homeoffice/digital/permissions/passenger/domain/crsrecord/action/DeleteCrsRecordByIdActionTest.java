package org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.action;


import org.gov.uk.homeoffice.digital.permissions.passenger.domain.crsrecord.CrsRecordDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DeleteCrsRecordByIdActionTest {

    @Test
    public void shouldDeleteCrsRecord() {
        final Long id = 123l;

        final Handle mockHandle = mock(Handle.class);
        final CrsRecordDAO mockDAO = mock(CrsRecordDAO.class);

        when(mockHandle.attach(CrsRecordDAO.class)).thenReturn(mockDAO);

        final DeleteCrsRecordByIdAction underTest = new DeleteCrsRecordByIdAction(id);
        underTest.useHandle(mockHandle);

        verify(mockDAO).delete(id);
    }

}