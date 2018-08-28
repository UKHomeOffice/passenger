package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class SaveVisaTypeActionTest {

    @Test
    public void shouldInsertVisaType() {
        final Handle mockHandle = mock(Handle.class);
        final VisaTypeDAO mockDAO = mock(VisaTypeDAO.class);

        final VisaType visaType = VisaType.createVisaType("test");

        when(mockHandle.attach(VisaTypeDAO.class)).thenReturn(mockDAO);
        when(mockDAO.insert(visaType)).thenReturn(500L);
        when(mockDAO.selectById(500L)).thenReturn(Optional.of(visaType));

        final SaveVisaTypeAction underTest = new SaveVisaTypeAction(visaType);
        underTest.withHandle(mockHandle);

        verify(mockDAO).insert(visaType);
    }

    @Test
    public void shouldUpdateVisaType() {
        final Handle mockHandle = mock(Handle.class);
        final VisaTypeDAO mockDAO = mock(VisaTypeDAO.class);

        final VisaType visaType = VisaType.createVisaType("test");
        visaType.setId(123L);

        when(mockHandle.attach(VisaTypeDAO.class)).thenReturn(mockDAO);
        when(mockDAO.selectById(123L)).thenReturn(Optional.of(visaType));

        final SaveVisaTypeAction underTest = new SaveVisaTypeAction(visaType);
        underTest.withHandle(mockHandle);

        verify(mockDAO).update(visaType);
    }

}