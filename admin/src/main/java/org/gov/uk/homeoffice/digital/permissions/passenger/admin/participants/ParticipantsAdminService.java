package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.ParseError;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload.ParsedResult;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.upload.ParticipantsParser;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.participant.ParticipantRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.email.NotifyService;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.service.notify.SendEmailResponse;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;

import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.CollectionUtils.add;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple.tpl;

@Service
public class ParticipantsAdminService {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    VisaRepository visaRepository;

    @Autowired
    NotifyService notifyService;

    @Autowired
    ParticipantsParser participantsParser;

    @Value("${app.url}")
    private String baseUrl;

    @Value("${email.enabled}")
    private Boolean emailEnabled;

    public ParsedResult process(File file, String currentUser) {
        final ParsedResult result = participantsParser.parse(file);
        final List<Tuple<Participant, Visa>> participants = new ArrayList<>(result.participants);
        final List<ParseError> parseErrors = new ArrayList<>(result.parseErrors);

        result.participants.forEach(participantAndVisa -> processParticipantAndVisa(participants, parseErrors, participantAndVisa, currentUser));

        return new ParsedResult(participants, parseErrors);
    }

    private void processParticipantAndVisa(List<Tuple<Participant, Visa>> participants, List<ParseError> parseErrors, Tuple<Participant, Visa> participantAndVisa, String currentUser) {
        try {
            participantRepository.save(participantAndVisa.get_1().withUpdatedBy(currentUser));
            visaRepository.save(participantAndVisa.get_2());
            if (emailEnabled) {
                Executors.newSingleThreadExecutor().submit(() -> sendVisaEmail(participantAndVisa));
            }
            participants.remove(participantAndVisa);
            participants.add(tpl(participantRepository.getById(participantAndVisa.get_1().getId()).orElseThrow(() -> new RuntimeException("no participant found")), participantAndVisa.get_2()));
        } catch (Exception e) {
            participants.remove(participantAndVisa);
            parseErrors.add(new ParseError("", e));
        }
    }

    void sendVisaEmail(Tuple<Participant, Visa> participantAndVisa) {

        final Set<String> emailsSent = participantRepository.getById(participantAndVisa.get_1().getId()).map(part -> part.getEmailsSent()).orElse(Collections.emptySet());
        if (visaGrantedToBeSend(participantAndVisa, emailsSent)) {
            final Optional<SendEmailResponse> response = notifyService.sendVisaGrantedEmail(participantAndVisa.get_1().getEmail(), participantAndVisa.get_2(), participantAndVisa.get_1(), baseUrl);
            if (response.isPresent()) {
                participantRepository.updateVisaEmailsSent(participantAndVisa.get_1().withEmailsSent(add(emailsSent, "GRANTED")));
            }
        }
        if (visaRevokedToBeSend(participantAndVisa, emailsSent)) {
            final Optional<SendEmailResponse> response = notifyService.sendVisaRevokedEmail(participantAndVisa.get_1().getEmail(), participantAndVisa.get_2(), participantAndVisa.get_1(), baseUrl);
            if (response.isPresent()) {
                participantRepository.updateVisaEmailsSent(participantAndVisa.get_1().withEmailsSent(add(emailsSent, "REVOKED")));
            }
        }

    }

    private boolean visaGrantedToBeSend(Tuple<Participant, Visa> participantAndVisa, Set<String> emailsSent) {
        return participantAndVisa.get_2().getStatus() == VisaStatus.VALID && !emailsSent.contains("GRANTED");
    }

    private boolean visaRevokedToBeSend(Tuple<Participant, Visa> participantAndVisa, Set<String> emailsSent) {
        return participantAndVisa.get_2().getStatus() == VisaStatus.REVOKED && !emailsSent.contains("REVOKED");
    }

}
