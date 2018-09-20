package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.*;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisaTypeServiceBeanTest {


    @Mock
    private VisaTypeRepository mockVisaTypeRepository;

    @Mock
    private VisaRuleLookupRepository mockVisaRuleLookupRepository;

    @InjectMocks
    private VisaTypeServiceBean underTest;

    @Test
    public void noVisaRuleFound() {
        VisaRecord visaRecord = new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("None"), tier4GeneralStudentRules());

        when(mockVisaTypeRepository.findAll()).thenReturn(visaTypeCollection());

        final Tuple<Optional<VisaTypeRule>, List<String>> visaTypeRule = underTest.findVisaTypeRule(visaRecord);
        Assert.assertThat(visaTypeRule.get_1().isPresent(), CoreMatchers.is(false));
        Assert.assertThat(visaTypeRule.get_2().get(0), CoreMatchers.is("No rules matched for input type None"));
    }

    @Test
    public void shouldFindSingleVisaRule() {
        VisaRecord visaRecord = new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("Tier 4 (General) Student"), tier4GeneralStudentRules());

        when(mockVisaTypeRepository.findAll()).thenReturn(visaTypeCollection());
        when(mockVisaRuleLookupRepository.findByVisaType(1L)).thenReturn(toVisaRules(tier4GeneralStudentRules()));

        final Tuple<Optional<VisaTypeRule>, List<String>> visaTypeRule = underTest.findVisaTypeRule(visaRecord);
        Assert.assertThat(visaTypeRule.get_1().isPresent(), CoreMatchers.is(true));
    }

    @Test
    public void shouldResolveMultipleVisaRules() {
        final VisaRecord visaRecord = new VisaRecord(VisaStatus.ISSUED, VisaType.createVisaType("Tier 4 (General) Dependent Child"),
                tier4GeneralDependentChild12MonthsMinusRules());

        when(mockVisaTypeRepository.findAll()).thenReturn(visaTypeCollection());
        when(mockVisaRuleLookupRepository.findByVisaType(6L)).thenReturn(toVisaRules(tier4GeneralDependentChild12MonthsPlusRules()));
        when(mockVisaRuleLookupRepository.findByVisaType(7L)).thenReturn(toVisaRules(tier4GeneralDependentChild12MonthsMinusRules()));

        final Tuple<Optional<VisaTypeRule>, List<String>> visaTypeRule = underTest.findVisaTypeRule(visaRecord);
        visaTypeRule.get_1().ifPresentOrElse(rule -> {
            assertTrue(rule.hasVisaRule("VALID_UNTIL"));
            assertTrue(rule.hasVisaRule("VISA_TYPE"));
            assertTrue(rule.hasVisaRule("NAME"));
            assertTrue(rule.hasVisaRule("PASSPORT_NUMBER"));
            assertTrue(rule.hasVisaRule("NATIONALITY"));
            assertTrue(rule.hasVisaRule("DATE_OF_BIRTH"));
            assertTrue(rule.hasVisaRule("CODE_3"));
            assertTrue(rule.hasVisaRule("ADDITIONAL_ENDORSEMENT"));
            assertTrue(rule.hasVisaRule("SPX_NUMBER"));
            assertTrue(rule.hasVisaRule("SPORTS"));
            assertTrue(rule.hasVisaRule("POLICE_REGISTRATION_VN"));
        }, () -> fail("Expected a visa type rule."));
    }

    private Collection<VisaType> visaTypeCollection() {
        return newArrayList(
                new VisaType(1L, "Tier 4 (General) Student", null, true, LocalDateTime.now()),
                new VisaType(2L, "Tier 4 (Child(S))", null, true, LocalDateTime.now()),
                new VisaType(3L, "Tier 4 (Child) Student 16-", null, true, LocalDateTime.now()),
                new VisaType(4L, "Tier 4 (Child) Student 16+", null, true, LocalDateTime.now()),
                new VisaType(5L, "Tier 4 (General(S)) Student", null, true, LocalDateTime.now()),
                new VisaType(6L, "Tier 4 (General) Dependent Child", "12 Months+", true, LocalDateTime.now()),
                new VisaType(7L, "Tier 4 (General) Dependent Child", "12 Months-", true, LocalDateTime.now())
        );
    }

    private Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> tier4GeneralStudentRules() {
        return newArrayList(
                new Tuple<>(new VisaRule("VALID_UNTIL"), null),
                new Tuple<>(new VisaRule("VISA_TYPE"), null),
                new Tuple<>(new VisaRule("NAME"), null),
                new Tuple<>(new VisaRule("PASSPORT_NUMBER"), null),
                new Tuple<>(new VisaRule("NATIONALITY"), null),
                new Tuple<>(new VisaRule("DATE_OF_BIRTH"), null),
                new Tuple<>(new VisaRule("CODE_3"), null),
                new Tuple<>(new VisaRule("CAS_NUMBER"), null),
                new Tuple<>(new VisaRule("SPX_NUMBER"), null),
                new Tuple<>(new VisaRule("POLICE_REGISTRATION_VN"), null)
        );
    }

    private Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> tier4GeneralDependentChild12MonthsPlusRules() {
        return newArrayList(
                new Tuple<>(new VisaRule("VALID_UNTIL"), null),
                new Tuple<>(new VisaRule("VISA_TYPE"), null),
                new Tuple<>(new VisaRule("NAME"), null),
                new Tuple<>(new VisaRule("PASSPORT_NUMBER"), null),
                new Tuple<>(new VisaRule("NATIONALITY"), null),
                new Tuple<>(new VisaRule("DATE_OF_BIRTH"), null),
                new Tuple<>(new VisaRule("CODE_1"), null),
                new Tuple<>(new VisaRule("ADDITIONAL_ENDORSEMENT"), null),
                new Tuple<>(new VisaRule("SPX_NUMBER"), null),
                new Tuple<>(new VisaRule("SPORTS"), null),
                new Tuple<>(new VisaRule("POLICE_REGISTRATION_VN"), null)
        );
    }

    private Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> tier4GeneralDependentChild12MonthsMinusRules() {
        return newArrayList(
                new Tuple<>(new VisaRule("VALID_UNTIL"), null),
                new Tuple<>(new VisaRule("VISA_TYPE"), null),
                new Tuple<>(new VisaRule("NAME"), null),
                new Tuple<>(new VisaRule("PASSPORT_NUMBER"), null),
                new Tuple<>(new VisaRule("NATIONALITY"), null),
                new Tuple<>(new VisaRule("DATE_OF_BIRTH"), null),
                new Tuple<>(new VisaRule("CODE_3"), null),
                new Tuple<>(new VisaRule("ADDITIONAL_ENDORSEMENT"), null),
                new Tuple<>(new VisaRule("SPX_NUMBER"), null),
                new Tuple<>(new VisaRule("SPORTS"), null),
                new Tuple<>(new VisaRule("POLICE_REGISTRATION_VN"), null)
        );
    }

    private Collection<VisaRule> toVisaRules(Collection<Tuple<VisaRule,  Collection<VisaRuleContent>>> visaRules) {
        return visaRules.stream().map(Tuple::get_1).collect(Collectors.toList());
    }


}
