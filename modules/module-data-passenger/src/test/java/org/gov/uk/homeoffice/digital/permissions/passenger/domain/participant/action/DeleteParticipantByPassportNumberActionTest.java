package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DeleteParticipantByPassportNumberActionTest {

    @Test
    public void shouldDeleteParticipant() {
        final String passportNumber = "123ABC456D";

        final Handle mockHandle = mock(Handle.class);
        final ParticipantDAO mockDAO = mock(ParticipantDAO.class);

        when(mockHandle.attach(ParticipantDAO.class)).thenReturn(mockDAO);

        final DeleteParticipantByPassportNumberAction underTest = new DeleteParticipantByPassportNumberAction(passportNumber);
        underTest.useHandle(mockHandle);

        verify(mockDAO).delete(passportNumber);
    }

}