package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.ParticipantBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class UpdateSentVisasActionTest {

    @Test
    public void shouldUpdateSentVisas() {
        final Participant participant = participant();

        final Handle mockHandle = mock(Handle.class);
        final ParticipantDAO mockDAO = mock(ParticipantDAO.class);

        when(mockHandle.attach(ParticipantDAO.class)).thenReturn(mockDAO);

        final UpdateSentVisasAction underTest = new UpdateSentVisasAction(participant);
        underTest.useHandle(mockHandle);

        verify(mockDAO).setEmailsSent(participant);
    }

    private Participant participant() {
        return new ParticipantBuilder()
                .setId(1L)
                .setGwf("GWF046027237")
                .setVaf("1295789")
                .setCas("E4G8DF0F36F0V1")
                .setFirstName("Simon")
                .setSurName("Kent")
                .setDateOfBirth(LocalDate.of(1996, 10, 22))
                .setNationality("GBR")
                .setPassportNumber("539701015")
                .setMobileNumber("6316032284")
                .setEmail("test1@test.com")
                .setInstitutionAddress("74 Academia Row, Haytown, Surrey, AB0 1CD")
                .createParticipant();
    }

}