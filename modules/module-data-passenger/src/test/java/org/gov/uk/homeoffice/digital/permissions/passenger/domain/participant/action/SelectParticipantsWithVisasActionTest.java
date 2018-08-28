package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.ParticipantBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantDAO;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectParticipantsWithVisasActionTest {

    @Test
    public void shouldSelectParticipantsWithVisas() {
        final VisaRepository mockVisaRepository = mock(VisaRepository.class);

        final Handle mockHandle = mock(Handle.class);
        final ParticipantDAO mockDAO = mock(ParticipantDAO.class);

        when(mockHandle.attach(ParticipantDAO.class)).thenReturn(mockDAO);
        when(mockDAO.getAll()).thenReturn(Collections.singletonList(participant()));

        final SelectParticipantsWithVisasAction underTest = new SelectParticipantsWithVisasAction(mockVisaRepository);
        underTest.withHandle(mockHandle);

        verify(mockVisaRepository).getByPassportNumber("539701015");
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