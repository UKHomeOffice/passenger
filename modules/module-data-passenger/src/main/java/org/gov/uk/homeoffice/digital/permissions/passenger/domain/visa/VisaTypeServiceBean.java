package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaTypeRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class VisaTypeServiceBean implements VisaTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisaTypeServiceBean.class);

    private VisaTypeRepository visaTypeRepository;
    private VisaRuleLookupRepository visaRuleLookupRepository;

    @Autowired
    public VisaTypeServiceBean(final VisaTypeRepository visaTypeRepository,
                               final VisaRuleLookupRepository visaRuleLookupRepository) {
        this.visaTypeRepository = visaTypeRepository;
        this.visaRuleLookupRepository = visaRuleLookupRepository;
    }

    @Override
    public Tuple<Optional<VisaTypeRule>, List<String>> findVisaTypeRule(final VisaRecord visaRecord) {
        LOGGER.debug("Locating visa type for {}", visaRecord.getVisaType() == null
                ? "<unspecified>" : visaRecord.getVisaType().getName());

        List<String> errors = new ArrayList<String>();

        final Collection<VisaType> visaTypes = visaTypeRepository.findAll();

        final Map<VisaType, Collection<VisaRule>> visaRulesForCurrentType = visaTypes.stream()
                // Filter out any visa types that don't match the name
                .filter(type -> isMatchingVisaType.test(type, visaRecord))
                .peek(type -> LOGGER.debug(":: matched visa type {}(id:{})", type.getName(), type.getId()))
                // Build a map for the visa type -> visa rules collection
                .collect(toMap(type -> type, this::visaRulesForType));

        if(visaRulesForCurrentType.isEmpty()){
            errors.add("No rules matched for input type " + visaRecord.getVisaType().getName());
            return new Tuple<>(Optional.empty(), errors);
        }

        final Optional<VisaTypeRule> matchedVisaTypeRule = visaRulesForCurrentType
                // Stream each entry
                .entrySet().stream()
                // Filter only entries that have all the business rules enabled exclude optional rules
                .filter(entry -> findMatchingVisaRule(visaRecord, entry))
                .peek(entry -> LOGGER.error(":: matched business rule on type {}(id:{})", entry.getKey().getName(),
                        entry.getKey().getId()))
                .sorted((x, y) -> x.getValue().size() > y.getValue().size() ? -1:+1)
                // ...and select the first match
                .findFirst()
                // Map to a visa type rule model
                .map(entry -> new VisaTypeRule(entry.getKey(), entry.getValue()));


        if (!matchedVisaTypeRule.isPresent()) {
            visaRulesForCurrentType
                    // Stream each entry
                    .entrySet().stream().forEach(r ->
            {
                final Set<String> inputRules = visaRecord.getVisaRules().stream().filter(rule -> rule.getEnabled()).map(rule -> rule.getRule()).collect(Collectors.toSet());
                final String nonMatchingRules = r.getValue().stream().filter(inputRule -> !inputRules.contains(inputRule.getRule())).map(inputRule -> inputRule.getRule()).collect(Collectors.joining(","));
                errors.add("Input type matched: " + visaRecord.getVisaType().getName() + ", Qualified Type (did not match): " + r.getKey().getNotes() + ", Rules that did not match qualified type: " + nonMatchingRules);
            });

        }

        return new Tuple<>(matchedVisaTypeRule, errors);

    }

    private boolean findMatchingVisaRule(VisaRecord visaRecord, Map.Entry<VisaType, Collection<VisaRule>> entry) {
        final Collection<VisaRule> allOptional = visaRuleLookupRepository.findAllOptional();
        return getMandatoryVisaRules(visaRecord.getVisaRules(), allOptional).containsAll(getMandatoryVisaRules(entry.getValue(), allOptional));
    }

    private Collection<VisaRule> getMandatoryVisaRules(Collection<VisaRule> visaRules, Collection<VisaRule> allOptional) {
        final Collection<VisaRule> allMandatoryVisaRules = new ArrayList<>();
        allMandatoryVisaRules.addAll(visaRules);
        allMandatoryVisaRules.removeAll(allOptional);
        return allMandatoryVisaRules;
    }

    private Collection<VisaRule> visaRulesForType(final VisaType visaType) {
        return visaRuleLookupRepository.findByVisaType(visaType.getId());
    }

    private static BiPredicate<VisaType, VisaRecord> isMatchingVisaType = (visaType, visaRecord) ->
            visaRecord.getVisaType() == null || visaType.getName().equalsIgnoreCase(visaRecord.getVisaType().getName().trim());

}
