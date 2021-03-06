package org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.model.Passenger;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Controller
public class DebugController {

    private VisaRecordService visaRecordService;
    private VisaRuleLookupRepository visaRuleLookupRepository;
    private Map<VisaType, Collection<VisaRule>> visaTypeCollectionMap;
    private final AuditService auditService;

    @Autowired
    public DebugController(final VisaRecordService visaRecordService,
                           final VisaTypeRepository visaTypeRepository,
                           final VisaRuleLookupRepository visaRuleLookupRepository,
                           @Qualifier("audit.admin") final AuditService auditService) {
        this.visaRecordService = visaRecordService;
        this.visaRuleLookupRepository = visaRuleLookupRepository;
        this.visaTypeCollectionMap = visaTypeRepository.findAll().stream()
                .collect(toMap(type -> type, type -> visaRuleLookupRepository.findByVisaType(type.getId())));
        this.auditService = auditService;
    }

    @RequestMapping(value = "/debug", method = RequestMethod.GET)
    public String displayDebug(Model model) {
        final Passenger passenger = new Passenger();
        model.addAttribute("passenger", passenger);
        return "rules/debug";
    }

    @RequestMapping(value = "/evaluatePassenger", method = RequestMethod.POST)
    public String evaluatePassenger(@ModelAttribute(value = "passenger") final Passenger passenger, final Model model) {
        if(invalidInput(passenger)){
            return notFound(model);
        }
        Optional<String> visaIdentifier = visaRecordService.getVisaIdentifier(passenger.getPassportNumber(), getDate(passenger));

        return visaIdentifier.map(id -> {
            final VisaRecord record = visaRecordService.get(id);
            final Map<VisaType, String> matchingRules = new HashMap<>();

            visaTypeCollectionMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getName().equalsIgnoreCase(record.getVisaType().getName().trim()) && findMatchingVisaRule(record.getVisaRules(), entry.getValue()))
                    .sorted((x,y) -> x.getValue().size() > y.getValue().size() ? -1:+1)
                    .limit(1)
                    .forEach(entry -> matchingRules.put(entry.getKey(), join(entry.getValue())));

            model.addAttribute("record", record);
            model.addAttribute("rules", matchingRules);

            auditService.audit("action='Debug participant'", "SUCCESS",
                    record.firstValueAsStringFor(VisaRuleConstants.FULL_NAME),
                    record.firstValueAsStringFor(VisaRuleConstants.EMAIL_ADDRESS),
                    record.firstValueAsStringFor(VisaRuleConstants.PASSPORT_NUMBER));

            return "rules/debug";
        }).orElseGet(() -> notFound(model));
    }

    private LocalDate getDate(Passenger passenger) {
        try {
            return LocalDate.of(
                    Integer.valueOf(passenger.getYear()),
                    Integer.valueOf(passenger.getMonth()),
                    Integer.valueOf(passenger.getDay()));
        } catch (Exception e) {

        }
        return null;
    }

    private boolean invalidInput(Passenger passenger) {
        return isEmpty(passenger.getPassportNumber()) || getDate(passenger) == null;
    }

    private String notFound(Model model) {
        model.addAttribute("error", "Not found");
        return "rules/debug";
    }

    private String join(Collection<VisaRule> rules) {
        return rules.stream()
                .map(VisaRule::getRule)
                .collect(Collectors.joining(", "));
    }

    private boolean findMatchingVisaRule(Collection<VisaRule> visaRulesFromVisaRecord, Collection<VisaRule> referenceVisaRules) {
        final Collection<VisaRule> allOptional = visaRuleLookupRepository.findAllOptional();
        return getMandatoryVisaRules(visaRulesFromVisaRecord, allOptional).containsAll(getMandatoryVisaRules(referenceVisaRules, allOptional));
    }

    private Collection<VisaRule> getMandatoryVisaRules(Collection<VisaRule> visaRules, Collection<VisaRule> allOptional) {
        final Collection<VisaRule> allMandatoryVisaRules = new ArrayList<>();
        allMandatoryVisaRules.addAll(visaRules);
        allMandatoryVisaRules.removeAll(allOptional);
        return allMandatoryVisaRules;
    }


}
