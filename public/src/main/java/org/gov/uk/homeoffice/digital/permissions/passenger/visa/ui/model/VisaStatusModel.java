package org.gov.uk.homeoffice.digital.permissions.passenger.visa.ui.model;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.visa.rule.DynamicContentProcessor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants.*;
import static org.gov.uk.homeoffice.digital.permissions.passenger.utils.DateTimeUtils.fromDisplayDate;

@EqualsAndHashCode
public class VisaStatusModel implements Serializable {

    private static final Log LOGGER = LogFactory.getLog(VisaStatusModel.class);

    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy");

    private final DynamicContentProcessor dynamicContentProcessor;
    private final VisaRecord record;
    private final VisaTypeRule typeRule;

    public VisaStatusModel(final DynamicContentProcessor dynamicContentProcessor,
                           final VisaRecord record,
                           final VisaTypeRule typeRule) {
        this.dynamicContentProcessor = dynamicContentProcessor;
        this.record = record;
        this.typeRule = typeRule;
    }

    public boolean isSPX() {
        return show(SPX_NUMBER);
    }

    public boolean isCAS() {
        return show(CAS_NUMBER);
    }


    public boolean isExpired() {
        return isValidUntil() && fromDisplayDate(VALID_UNTIL).isBefore(LocalDate.now());
    }

    public boolean isRevoked() {
        return record.getVisaStatus().equals(VisaStatus.REFUSED);
    }

    public boolean isGranted() {
        return record.getVisaStatus().equals(VisaStatus.ISSUED) && !isExpired();
    }

    public boolean isLongTerm() {
        return isValidUntil() && fromDisplayDate(VALID_UNTIL).isAfter(fromDisplayDate(VALID_FROM).plusMonths(6));
    }

    public boolean isShortTerm() {
        return isValidUntil() && fromDisplayDate(VALID_UNTIL).isBefore(fromDisplayDate(VALID_FROM).plusMonths(6));
    }

    public boolean isName() {
        return show(VisaRuleConstants.FULL_NAME);
    }

    public boolean isPassportNumber() {
        return show(PASSPORT_NUMBER);
    }

    public boolean isDateOfBirth() {
        return show(DATE_OF_BIRTH);
    }

    public boolean isValidFrom() {
        return show(VALID_FROM);
    }

    public boolean isValidUntil() {
        return show(VALID_UNTIL);
    }

    public boolean isCode1() {
        return show(CODE_1);
    }

    public boolean isCode1A() {
        return show(CODE_1A);
    }

    public boolean isCode2() {
        return show(CODE_2);
    }

    public boolean isCode3() {
        return show(CODE_3);
    }

    public boolean isCode4() {
        return show(CODE_4);
    }

    public boolean isCode7() {
        return show(CODE_7);
    }

    public boolean isAdditionalEndorsement() {
        return show(ADDITIONAL_ENDORSEMENT);
    }

    public boolean isSpxNumber() {
        return show(SPX_NUMBER);
    }

    public boolean isCasNumber() {
        return show(CAS_NUMBER);
    }

    public boolean isCosNumber() {
        return show(COS_NUMBER);
    }

    public boolean isBusiness() {
        return show(BUSINESS);
    }

    public boolean isSports() {
        return show(SPORTS);
    }

    public boolean isDoctor() {
        return show(DOCTOR);
    }

    public boolean isWorking10Hours() {
        return show(WORKING_10_HOURS);
    }

    public boolean isWorking20Hours() {
        return show(WORKING_20_HOURS);
    }

    public boolean isPoliceRegistration() {
        return record.getVisaRulesMapping().stream().anyMatch(v -> v.get_1().getRule().equals(POLICE_REGISTRATION_VN) ||  v.get_1().getRule().equals(POLICE_REGISTRATION_NVN));
    }

    public boolean hasReason() {
        return show(REASON);
    }

    public boolean isNationality() {
        return show(NATIONALITY);
    }

    public boolean isSponsorName() {
        return show(SPONSOR_NAME);
    }

    public boolean isSponsorType() {
        return show(SPONSOR_TYPE);
    }

    public boolean isSponsorAddress() {
        return show(SPONSOR_ADDRESS);
    }

    public String casNumber() {
        return singleValueFor(CAS_NUMBER);
    }

    public Collection<String> code1() {
        return stringValuesFor(CODE_1);
    }

    public Collection<String> code1A() {
        return stringValuesFor(CODE_1A);
    }

    public Collection<String> code2() {
        return stringValuesFor(CODE_2);
    }

    public Collection<String> code3() {
        return stringValuesFor(CODE_3);
    }

    public Collection<String> code4() {
        return stringValuesFor(CODE_4);
    }

    public Collection<String> code7() {
        return stringValuesFor(CODE_7);
    }

    public String additionalEndorsement() {
        return singleValueFor(ADDITIONAL_ENDORSEMENT);
    }

    public String spxNumber() {
        return singleValueFor(SPX_NUMBER);
    }

    public String cosNumber() {
        return singleValueFor(COS_NUMBER);
    }

    public String validFrom() {
        return OUTPUT_FORMATTER.format(fromDisplayDate(singleValueFor(VALID_FROM)));
    }

    public String validFromPlusThirtyDays() {
        return OUTPUT_FORMATTER.format(fromDisplayDate(VALID_FROM).plusDays(30));
    }

    public String validUntil() {
        return OUTPUT_FORMATTER.format(fromDisplayDate(singleValueFor(VALID_UNTIL)));
    }

    public String name() {
        return singleValueFor(FULL_NAME);
    }

    public String passportNumber() {
        return singleValueFor(PASSPORT_NUMBER);
    }

    public String dateOfBirth() {
        return OUTPUT_FORMATTER.format(fromDisplayDate(singleValueFor(DATE_OF_BIRTH)));
    }

    public String reason() {
        return singleValueFor(REASON);
    }

    public Collection<String> business() {
        return stringValuesFor(BUSINESS);
    }

    public Collection<String> sports() {
        return stringValuesFor(SPORTS);
    }

    public Collection<String> doctor() {
        return stringValuesFor(DOCTOR);
    }

    public Collection<String> working10Hours() {
        return stringValuesFor(WORKING_10_HOURS);
    }

    public Collection<String> working20Hours() {
        return stringValuesFor(WORKING_20_HOURS);
    }

    public String nationality() {
        return singleValueFor(NATIONALITY);
    }

    public String status() {
        return record.getVisaStatus().name();
    }

    public String visaType() {
        return typeRule.getVisaType();
    }

    public Collection<String> policeRegistration() {
        if (show(POLICE_REGISTRATION_NVN)) {
            return stringValuesFor(POLICE_REGISTRATION_NVN);
        }
        else {
            return stringValuesFor(POLICE_REGISTRATION_VN);
        }
    }

    public String sponsorName() {
        return singleValueFor(SPONSOR_NAME);
    }

    public String sponsorType() {
        return singleValueFor(SPONSOR_TYPE);
    }

    public String sponsorAddress() {
        return singleValueFor(SPONSOR_ADDRESS);
    }

    public Collection<VisaRuleContent> allValuesFor(final String ruleTypeStr) {
        final RuleType ruleType = RuleType.valueOf(ruleTypeStr);
        return allValues().stream()
                .filter(value -> value.getRuleType().equals(ruleType))
                .collect(Collectors.toList());
    }

    private Collection<VisaRuleContent> allValues() {
        final Set<VisaRuleContent> allValues = new HashSet<>();
        allValues.addAll(valuesFor(CODE_1));
        allValues.addAll(valuesFor(CODE_1A));
        allValues.addAll(valuesFor(CODE_2));
        allValues.addAll(valuesFor(CODE_3));
        allValues.addAll(valuesFor(CODE_4));
        allValues.addAll(valuesFor(CODE_7));
        allValues.addAll(valuesFor(WORKING_10_HOURS));
        allValues.addAll(valuesFor(WORKING_20_HOURS));
        allValues.addAll(valuesFor(SPORTS));
        allValues.addAll(valuesFor(BUSINESS));
        allValues.addAll(valuesFor(DOCTOR));
        allValues.addAll(valuesFor(POLICE_REGISTRATION_NVN));
        return allValues;
    }

    private String singleValueFor(final String rule) {
        return record.getVisaRulesMapping().stream()
                .filter(tpl -> tpl.get_1().getRule().equals(rule))
                .flatMap(tpl -> tpl.get_2().stream())
                .filter(Objects::nonNull)
                .filter(content -> Objects.nonNull(content.getContent()))
                .map(content -> dynamicContentProcessor.transform(content.getContent(), this))
                .findFirst()
                .orElse(null);
    }

    private Collection<String> stringValuesFor(final String rule) {
        return valuesFor(rule).stream()
                .map(VisaRuleContent::getContent)
                .collect(Collectors.toList());
    }

    private Collection<VisaRuleContent> valuesFor(final String rule) {
        return record.getVisaRulesMapping().stream()
                .peek(tpl -> LOGGER.debug("Rule: " + tpl.get_1().getRule()))
                .filter(tpl -> tpl.get_1().getRule().equals(rule))
                .flatMap(tpl -> tpl.get_2().stream())
                .filter(Objects::nonNull)
                .map(content -> dynamicContentProcessor.transform(content, this))
                .collect(Collectors.toList());
    }

    private boolean show(final String rule) {
        return typeRule.hasVisaRule(rule);
    }

    public boolean hasPositiveEndorsements(){
        return this.allValuesFor("POSITIVE").size() > 0;
    }

    public boolean hasNegativeEndorsements(){
        return this.allValuesFor("NEGATIVE").size() > 0;
    }

    public boolean hasNote(){
        return this.allValuesFor("NOTE").size() > 0;
    }

}
