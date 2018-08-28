package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.ParseError;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload.ParsedResult;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload.ParticipantsParser;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.service.notify.SendEmailResponse;

import java.io.File;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.CollectionUtils.set;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantsAdminServiceTest {


    @Mock
    ParticipantsParser participantsParser;

    @Mock
    ParticipantRepository participantRepository;

    @Mock
    VisaRepository visaRepository;

    @Mock
    NotifyService notifyService;

    @InjectMocks
    ParticipantsAdminService participantsAdminService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(participantsAdminService, "emailEnabled", Boolean.TRUE);
    }

    @Test
    public void parseFileAndSaveParticipants() {
        String currentUser = "test@test.com";
        File file = new File("test.csv");
        file.deleteOnExit();

        Participant participant1 = new ParticipantBuilder().withDefaults().setId(1L).setPassportNumber("ABC").createParticipant();
        Visa visa1 = new VisaBuilder().setPassportNumber("ABC").createVisa();

        Participant participant2 = new ParticipantBuilder().withDefaults().setId(2L).setPassportNumber("DEF").createParticipant();
        Visa visa2 = new VisaBuilder().setPassportNumber("DEF").createVisa();


        ParseError parseError1 = new ParseError("row1", new RuntimeException("it's all gone wrong!"));
        ParseError parseError2 = new ParseError("row2", new RuntimeException("it's all gone wrong!"));

        final Participant participantWithEmailSent = participant1.withEmailsSent(set("GRANTED"));
        when(participantRepository.getById(participant1.getId())).thenReturn(Optional.of(participantWithEmailSent));
        when(participantRepository.getById(participant2.getId())).thenReturn(Optional.of(participant2));


        ParsedResult result = new ParsedResult(asList(tpl(participant1, visa1), tpl(participant2, visa2)), asList(parseError1, parseError2));
        when(participantsParser.parse(file)).thenReturn(result);


        final ParsedResult actualResult = participantsAdminService.process(file, currentUser);

        assertThat(actualResult, is(new ParsedResult(asList(tpl(participantWithEmailSent, visa1), tpl(participant2, visa2)), asList(parseError1, parseError2))));

        verify(participantRepository).save(participant1.withUpdatedBy(currentUser));
        verify(visaRepository).save(visa1);
        verify(participantRepository).save(participant2.withUpdatedBy(currentUser));
        verify(visaRepository).save(visa2);


    }

    @Test
    public void amendResultIfErrorsWhileSaving() {

        File file = new File("test.csv");
        file.deleteOnExit();

        Participant participant1 = new ParticipantBuilder().withDefaults().setId(1L).setPassportNumber("ABC").createParticipant();
        Visa visa1 = new VisaBuilder().setPassportNumber("ABC").createVisa();

        Participant participant2 = new ParticipantBuilder().withDefaults().setId(2L).setPassportNumber("DEF").createParticipant();
        Visa visa2 = new VisaBuilder().setPassportNumber("DEF").createVisa();

        ParseError parseError1 = new ParseError("row1", new RuntimeException("it's all gone wrong!"));
        ParseError parseError2 = new ParseError("row2", new RuntimeException("it's all gone wrong!"));
        ParsedResult result = new ParsedResult(asList(tpl(participant1, visa1), tpl(participant2, visa2)), asList(parseError1, parseError2));

        when(participantsParser.parse(file)).thenReturn(result);

        final RuntimeException runtimeException = new RuntimeException("AARGH! It's all gone wrong!");
        doThrow(runtimeException).when(participantRepository).save(participant1);

        final ParsedResult updatedResult = result.withParticipants(singletonList(tpl(participant2, visa2)))
                .withParseErrors(asList(parseError1, parseError2, new ParseError("", runtimeException)));

        when(participantRepository.getById(participant2.getId())).thenReturn(Optional.of(participant2));

        assertThat(participantsAdminService.process(file, null), is(updatedResult));

        verify(participantRepository).save(participant2);
        verify(visaRepository).save(visa2);
    }

    @Test
    public void updateVisaGrantedEmailSentIfSuccessful() {
        final String emailAddress = "test@est.com";
        final Visa visa = new VisaBuilder().setSpx("spx").setStatus(VisaStatus.VALID).createVisa();
        final Participant participant = new ParticipantBuilder().withDefaults().setEmail(emailAddress).createParticipant();
        when(participantRepository.getById(participant.getId())).thenReturn(Optional.empty());
        when(notifyService.sendVisaGrantedEmail(emailAddress, visa, participant, null)).thenReturn(Optional.of(mock(SendEmailResponse.class)));
        participantsAdminService.sendVisaEmail(tpl(participant, visa));
        verify(participantRepository).updateVisaEmailsSent(participant.withEmailsSent(set("GRANTED")));
    }

    @Test
    public void doNotUpdateVisaGrantedEmailSentIfNotSuccessful() {
        final String emailAddress = "test@est.com";
        final Visa visa = new VisaBuilder().setSpx("spx").setStatus(VisaStatus.VALID).createVisa();
        final Participant participant = new ParticipantBuilder().withDefaults().setEmail(emailAddress).createParticipant();
        when(participantRepository.getById(participant.getId())).thenReturn(Optional.empty());
        when(notifyService.sendVisaGrantedEmail(emailAddress, visa, participant, null)).thenReturn(Optional.empty());
        participantsAdminService.sendVisaEmail(tpl(participant, visa));
        verify(participantRepository, never()).updateVisaEmailsSent(participant.withEmailsSent(set("GRANTED")));
    }

    @Test
    public void doNotSendVisaGrantedEmailIfAlreadySent() {

        final String emailAddress = "test@est.com";
        final Visa visa = new VisaBuilder().setSpx("spx").setStatus(VisaStatus.VALID).createVisa();
        final Participant participant = new ParticipantBuilder().withDefaults().setEmail(emailAddress).createParticipant();
        when(participantRepository.getById(participant.getId())).thenReturn(Optional.of(new ParticipantBuilder().withDefaults().setEmailsSent(set("GRANTED")).createParticipant()));

        participantsAdminService.sendVisaEmail(tpl(participant, visa));
        verify(notifyService, never()).sendVisaGrantedEmail(emailAddress, visa, participant, null);

    }

    @Test
    public void updateVisaRevokedEmailSentIfSuccessful() {
        final String emailAddress = "test@est.com";
        final Visa visa = new VisaBuilder().setSpx("spx").setStatus(VisaStatus.REVOKED).createVisa();
        final Participant participant = new ParticipantBuilder().withDefaults().setEmail(emailAddress).createParticipant();
        when(participantRepository.getById(participant.getId())).thenReturn(Optional.of(new ParticipantBuilder().withDefaults().setEmail(emailAddress).setEmailsSent(set("GRANTED")).createParticipant()));
        when(notifyService.sendVisaRevokedEmail(emailAddress, visa, participant, null)).thenReturn(Optional.of(mock(SendEmailResponse.class)));
        participantsAdminService.sendVisaEmail(tpl(participant, visa));
        verify(participantRepository).updateVisaEmailsSent(participant.withEmailsSent(set("GRANTED", "REVOKED")));
    }

    @Test
    public void doNotUpdateRevokedEmailSentIfNotSuccessful() {
        final String emailAddress = "test@est.com";
        final Visa visa = new VisaBuilder().setSpx("spx").setStatus(VisaStatus.REVOKED).createVisa();
        final Participant participant = new ParticipantBuilder().withDefaults().setEmail(emailAddress).createParticipant();
        when(participantRepository.getById(participant.getId())).thenReturn(Optional.of(new ParticipantBuilder().withDefaults().setEmail(emailAddress).setEmailsSent(set("GRANTED")).createParticipant()));
        when(notifyService.sendVisaRevokedEmail(emailAddress, visa, participant, null)).thenReturn(Optional.empty());
        participantsAdminService.sendVisaEmail(tpl(participant, visa));
        verify(participantRepository, never()).updateVisaEmailsSent(participant.withEmailsSent(set("GRANTED", "REVOKED")));
    }


    @Test
    public void doNotSendRevokedEmailIfAlreadySent() {

        final String emailAddress = "test@est.com";
        final Visa visa = new VisaBuilder().setSpx("spx").setStatus(VisaStatus.REVOKED).createVisa();
        final Participant participant = new ParticipantBuilder().withDefaults().setEmail(emailAddress).createParticipant();
        when(participantRepository.getById(participant.getId())).thenReturn(Optional.of(new ParticipantBuilder().withDefaults().setEmailsSent(set("GRANTED", "REVOKED")).createParticipant()));

        participantsAdminService.sendVisaEmail(tpl(participant, visa));
        verify(notifyService, never()).sendVisaRevokedEmail(emailAddress, visa, participant, null);

    }


}

