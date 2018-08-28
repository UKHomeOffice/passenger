package org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.controller;

import org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions.NotFoundException;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.model.Passenger;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaTypeRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord.VisaRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Controller
public class DebugController {

    private VisaRecordService visaRecordService;
    private VisaTypeRepository visaTypeRepository;
    private VisaRuleLookupRepository visaRuleLookupRepository;
    private Map<VisaType, Collection<VisaRule>> visaTypeCollectionMap;

    @Autowired
    public DebugController(final VisaRecordService visaRecordService,
                           final VisaTypeRepository visaTypeRepository,
                           final VisaRuleLookupRepository visaRuleLookupRepository) {
        this.visaRecordService = visaRecordService;
        this.visaTypeRepository = visaTypeRepository;
        this.visaRuleLookupRepository = visaRuleLookupRepository;
        this.visaTypeCollectionMap = visaTypeRepository.findAll().stream()
                .collect(toMap(type -> type, type -> visaRuleLookupRepository.findByVisaType(type.getId())));
    }

    @RequestMapping(value = "/debug", method = RequestMethod.GET)
    public String displayDebug(Model model) {
        final Passenger passenger = new Passenger();
        model.addAttribute("passenger", passenger);
        return "rules/debug";
    }

    @RequestMapping(value = "/evaluatePassenger", method = RequestMethod.POST)
    public String evaluatePassenger(@ModelAttribute(value = "passenger") final Passenger passenger, final Model model) {
        Optional<String> visaIdentifier = visaRecordService.getVisaIdentifier(
                passenger.getPassportNumber(),
                LocalDate.of(
                        Integer.valueOf(passenger.getYear()),
                        Integer.valueOf(passenger.getMonth()),
                        Integer.valueOf(passenger.getDay())));

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

            return "rules/debug";
        }).orElseGet(() -> notFound(model));
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
