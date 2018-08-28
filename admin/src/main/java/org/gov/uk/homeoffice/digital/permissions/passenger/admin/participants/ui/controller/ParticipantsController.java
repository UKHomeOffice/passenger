package org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.SecurityUtil;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participantadapter.ParticipantRecordRepositoryBean;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.participants.ui.model.ParticipantModelAdapter;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Participant;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.Visa;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ParticipantsController {

    private final ParticipantRecordRepositoryBean participantRepository;
    private final ParticipantModelAdapter participantModelAdapter;
    private final AuditService auditService;

    public ParticipantsController(final ParticipantRecordRepositoryBean participantRecordRepositoryBean,
                                  final ParticipantModelAdapter participantModelAdapter,
                                  @Qualifier("audit.admin") final AuditService auditService) {
        this.participantRepository = participantRecordRepositoryBean;
        this.participantModelAdapter = participantModelAdapter;
        this.auditService = auditService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/participant", method = GET)
    public String viewParticipantDetails(@RequestParam("id") final String id, final Map<String, Object> model) {
        model.put("participants", Collections.singletonList(participantRepository.getParticipantWithVisa(Long.valueOf(id))
                .map(pv -> participantModelAdapter.from(pv.get_1(), pv.get_2(), false)).orElse(null)));
        return "participants/participants";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/participants", method = GET)
    public String viewParticipants(Map<String, Object> model) {
        final List<Tuple<Participant, Visa>> uploaded = (List<Tuple<Participant, Visa>>) model.getOrDefault("uploaded", emptyList());

        model.put("participants", participantRepository.getAllParticipantsWithVisas().stream()
                .map(participant -> participantModelAdapter.from(participant.get_1(), participant.get_2(), isUploaded(participant.get_1().getId(), uploaded))).collect(toList()));

        return "participants/participants-table";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/deleteparticipant/{passportNumber}", method = POST)
    public String delete(@PathVariable("passportNumber") String passportNumber, Authentication authentication) {
        Optional<Participant> participant = participantRepository.getByPassportNumber(passportNumber);

        participantRepository.deleteParticipantWithPassportNumber(passportNumber);
        auditService.audit(
                String.format("action='delete', entity='Participant', id='%s'", participant.map(p -> p.getId()).orElse(null)),
                "SUCCESS", SecurityUtil.username());

        return "redirect:/participants";
    }

    private boolean isUploaded(long participantId, final List<Tuple<Participant, Visa>> uploaded) {
        return uploaded.stream().anyMatch(part -> part.get_1().getId() == participantId);
    }

}

