package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.RuleType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleContentRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;

public abstract class AbstractVisaRecordAdapter implements VisaRecordAdapter<String> {

    private final VisaRuleLookupRepository visaRuleLookupRepository;
    private final VisaRuleContentRepository visaRuleContentRepository;

    protected AbstractVisaRecordAdapter(final VisaRuleLookupRepository visaRuleLookupRepository,
                                        final VisaRuleContentRepository visaRuleContentRepository) {
        this.visaRuleLookupRepository = visaRuleLookupRepository;
        this.visaRuleContentRepository = visaRuleContentRepository;
    }

    /*
     * Overloaded helper method to convert user data to VisaRuleContent
     */
    protected Tuple<VisaRule, Collection<VisaRuleContent>> ruleFor(final String ruleName,
                                                                   final String content) {

        return ruleFor(ruleName, userDataContentFor(ruleName, content));
    }

    protected Tuple<VisaRule, Collection<VisaRuleContent>> ruleFor(final String ruleName) {
        return ruleFor(ruleName, (VisaRuleContent) null);
    }

    protected Tuple<VisaRule, Collection<VisaRuleContent>> ruleFor(final String ruleName,
                                                                   final VisaRuleContent content) {

        final VisaRule rule = findVisaRule(ruleName);
        if (rule != null) {
            final Collection<VisaRuleContent> visaRuleContents = ofNullable(content).map(List::of)
                    .orElse(newArrayList(findVisaRuleContent(ruleName)));

            return new Tuple<>(rule, visaRuleContents);
        }
        else {
            return new Tuple<>(rule, Collections.emptyList());
        }
    }

    /*
     * Helper method to create a visa rule content object from a rule / content provided by the user.
     */
    private VisaRuleContent userDataContentFor(final String ruleName,
                                               final String content) {
        return new VisaRuleContent(-1L, ruleName, content, true, RuleType.USER_DATA);
    }

    /*
     * Method requires update from database as the business rule values can be changed in the admin
     * but the instance within public will not reflect the changes.
     */
    private VisaRule findVisaRule(final String ruleName) {
        return visaRuleLookupRepository.findOneByRule(ruleName).orElse(null);
    }

    private Collection<VisaRuleContent> findVisaRuleContent(final String ruleName) {
        return visaRuleContentRepository.findByRule(ruleName);
    }

}
