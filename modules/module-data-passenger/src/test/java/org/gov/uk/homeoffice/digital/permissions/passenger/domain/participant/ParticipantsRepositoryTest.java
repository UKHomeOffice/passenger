package org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.ParticipantBuilder;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action.SaveOrUpdateAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action.SelectByIdAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.action.SelectParticipantsWithVisasAction;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ParticipantsRepositoryTest {

    @Mock
    private Jdbi mockJdbi;

    @Mock
    private VisaRepository mockVisaRepository;

    @InjectMocks
    private ParticipantRepositoryBean participantRepository;

    @Test
    public void saveParticipant() {
        final Participant participant = new ParticipantBuilder().withDefaults()
                .setId(1L)
                .setPassportNumber("ab")
                .createParticipant();

        participantRepository.save(participant);
        verify(mockJdbi).useTransaction(new SaveOrUpdateAction(participant));
    }

    @Test
    public void loadParticipant() {
        final long id = 1L;

        participantRepository.getById(id);
        verify(mockJdbi).withHandle(new SelectByIdAction(id));
    }

    @Test
    public void getAll() {
        participantRepository.getAllParticipantsWithVisas();
        verify(mockJdbi).withHandle(new SelectParticipantsWithVisasAction(mockVisaRepository));
    }

}
