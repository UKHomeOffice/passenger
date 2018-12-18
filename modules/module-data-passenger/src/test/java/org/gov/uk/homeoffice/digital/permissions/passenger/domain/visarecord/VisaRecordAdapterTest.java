package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visarecord;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.RuleType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleConstants;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleContentRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupRepository;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

public class VisaRecordAdapterTest {

    private void prepareMockFor(final String visaRule, final String content) {
        when(visaRuleLookupRepository.findOneByRule(visaRule)).thenReturn(Optional.of(new VisaRule(visaRule)));
        if (content != null) {
            when(visaRuleContentRepository.findByRule(visaRule)).thenReturn(newArrayList(new VisaRuleContent(-1L, visaRule, content, true, RuleType.USER_DATA)));
        }
    }

    protected void initVisaRuleLookupRepositoryMock() {
        prepareMockFor("PLACE_OF_ISSUE", "place-of-issue");
        prepareMockFor("VALID_FROM", "01012010");
        prepareMockFor("VALID_UNTIL", "02012010");
        prepareMockFor("WORK_UNTIL", "");
        prepareMockFor("NUMBER_OF_ENTRIES", "number-of-entries");
        prepareMockFor("VAF_NUMBER", "vaf-number");
        prepareMockFor("VISA_TYPE", "visa-type");
        prepareMockFor("NAME", "name");
        prepareMockFor("PASSPORT_NUMBER", null);
        prepareMockFor("GENDER", "MALE");
        prepareMockFor("DATE_OF_BIRTH", "01011998");
        prepareMockFor("NATIONALITY", "nationality");
        prepareMockFor("CODE_1", "code1");
        prepareMockFor("CODE_1A", "code1a");
        prepareMockFor("CODE_2", "code2");
        prepareMockFor("CODE_3", "code3");
        prepareMockFor("CODE_4", "code4");
        prepareMockFor("CODE_7", "code7");
        prepareMockFor("ADDITIONAL_ENDORSEMENT", "additional-endorsement");
        prepareMockFor("COS_NUMBER", "cos-number");
        prepareMockFor("CAS_NUMBER", "cas-number");
        prepareMockFor("SPX_NUMBER", "spx-number");
        prepareMockFor("10_HOURS", "10-hours");
        prepareMockFor("20_HOURS", "20-hours");
        prepareMockFor("SPORTS", "sports");
        prepareMockFor("DOCTOR", "doctor");
        prepareMockFor("BUSINESS", "business");
        prepareMockFor("POLICE_REGISTRATION_NVN", "police-registration-nvn");
        prepareMockFor("POLICE_REGISTRATION_VN", "police-registration-vn");
        prepareMockFor("FULL_NAME", "full-name");
        prepareMockFor("SURNAME", "surname");
        prepareMockFor("REASON", "reason");
        prepareMockFor("SPONSOR_NAME", "sponsor-name");
        prepareMockFor("SPONSOR_TYPE", "sponsor-type");
        prepareMockFor("SPONSOR_ADDRESS", "sponsor-address");
        prepareMockFor("UNI_COLLEGE_NAME", "uni-college-name");
        prepareMockFor("BRP_COLLECTION_INFO", "brp-collection-info");
        prepareMockFor("MOBILE_NUMBER", "mobile-number");
        prepareMockFor("EMAIL_ADDRESS", "email-address");
    }

    @Mock
    protected VisaRuleLookupRepository visaRuleLookupRepository;

    @Mock
    protected VisaRuleContentRepository visaRuleContentRepository;

    protected <T> Collection<T> merge(Collection<T> c, T... e) {
        return Stream.concat(c.stream(), Arrays.stream(e)).collect(Collectors.toSet());
    }

    protected Tuple<VisaRule, Collection<VisaRuleContent>> editableTuple(String rule, String display) {
        return Tuple.tpl(new VisaRule(rule), List.of(new VisaRuleContent(-1L, rule, display, true, RuleType.USER_DATA)));
    }

    protected Tuple<VisaRule, Collection<VisaRuleContent>> nonEditableTuple(String rule, String display) {
        return Tuple.tpl(new VisaRule(rule), List.of(new VisaRuleContent(-1L, rule, display, true, RuleType.USER_DATA)));
    }

    @Before
    public void setup() {
        visaRuleLookupRepository = Mockito.mock(VisaRuleLookupRepository.class);
        visaRuleContentRepository = Mockito.mock(VisaRuleContentRepository.class);

        initVisaRuleLookupRepositoryMock();
    }

    @Test
    public void ruleFor_ruleWithDisplayContent_displayValueNull() {
        MockVisaRecordAdapter a = new MockVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository);
        Tuple<VisaRule, Collection<VisaRuleContent>> t = a.ruleFor(VisaRuleConstants.CODE_1);

        assertThat(t.get_1(), is(new VisaRule("CODE_1")));
        assertThat(t.get_2(), is(List.of(new VisaRuleContent(-1L, "CODE_1", "code1", true, RuleType.USER_DATA))));
    }

    @Test
    public void ruleFor_ruleWithDisplayContent_displayValueNotNull() {
        MockVisaRecordAdapter a = new MockVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository);
        Tuple<VisaRule, Collection<VisaRuleContent>> t = a.ruleFor(VisaRuleConstants.CODE_1);

        assertThat(t.get_1(), is(new VisaRule("CODE_1")));
        assertThat(t.get_2(), is(List.of(new VisaRuleContent(-1L, "CODE_1", "code1", true, RuleType.USER_DATA))));
    }

    @Test
    public void ruleFor_ruleWithNoDisplayContent_displayValueNotNull() {
        MockVisaRecordAdapter a = new MockVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository);
        Tuple<VisaRule, Collection<VisaRuleContent>> t = a.ruleFor(VisaRuleConstants.PASSPORT_NUMBER);

        assertThat(t.get_1(), is(new VisaRule("PASSPORT_NUMBER")));
        assertThat(t.get_2(), is(Collections.emptyList()));
    }

    @Test
    public void ruleFor_ruleWithNoDisplayContent_displayValueNull() {
        MockVisaRecordAdapter a = new MockVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository);
        Tuple<VisaRule, Collection<VisaRuleContent>> t = a.ruleFor(VisaRuleConstants.PASSPORT_NUMBER);

        assertThat(t.get_1(), is(new VisaRule("PASSPORT_NUMBER")));
        assertThat(t.get_2(), is(Collections.emptyList()));
    }

    @Test
    public void ruleFor_ruleNotFound_displayValueNotNull() {
        MockVisaRecordAdapter a = new MockVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository);
        Tuple<VisaRule, Collection<VisaRuleContent>> t = a.ruleFor("NON_EXISTING_RULE","some text");

        assertThat(t.get_1(), is(nullValue()));
        assertThat(t.get_2(), is(Collections.emptyList()));
    }

    @Test
    public void ruleFor_ruleNotFound_displayValueNull() {
        MockVisaRecordAdapter a = new MockVisaRecordAdapter(visaRuleLookupRepository, visaRuleContentRepository);
        Tuple<VisaRule, Collection<VisaRuleContent>> t = a.ruleFor("NON_EXISTING_RULE");

        assertThat(t.get_1(), is(nullValue()));
        assertThat(t.get_2(), is(Collections.emptyList()));
    }

    private static class MockVisaRecordAdapter extends AbstractVisaRecordAdapter {

        protected MockVisaRecordAdapter(VisaRuleLookupRepository visaRuleLookupRepository,
                                        VisaRuleContentRepository visaRuleContentRepository) {
            super(visaRuleLookupRepository, visaRuleContentRepository);
        }

        public Tuple<VisaRule, Collection<VisaRuleContent>> ruleFor(final String ruleName, final String displayValue) {
            return super.ruleFor(ruleName, displayValue);
        }

        @Override
        public VisaRecord toVisaRecord(String s) {
            return null;
        }

        @Override
        public Collection<VisaRecord> getValidWithinRange(LocalDate lowerValidDate, LocalDate upperValidDate) {
            return null;
        }

        @Override
        public Optional<String> getIdentifier(String passportNumber, LocalDate dateOfBirth) {
            return Optional.empty();
        }
    }
}
