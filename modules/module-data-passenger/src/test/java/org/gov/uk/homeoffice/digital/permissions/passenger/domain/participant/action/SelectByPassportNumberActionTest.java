package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectByPassportNumberActionTest {

    @Test
    public void shouldSelectByPassportNumber() {
        final String passportNumber = "passportNumber";

        final Handle mockHandle = mock(Handle.class);
        final ParticipantDAO mockDAO = mock(ParticipantDAO.class);

        when(mockHandle.attach(ParticipantDAO.class)).thenReturn(mockDAO);

        final SelectByPassportNumberAction underTest = new SelectByPassportNumberAction(passportNumber);
        underTest.withHandle(mockHandle);

        verify(mockDAO).getByPassportNumber(passportNumber);
    }

}