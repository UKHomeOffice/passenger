package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaStatus;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaType;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaTypeRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VisaRuleMatcherBeanTest {

    @Mock
    private VisaTypeServiceBean mockVisaTypeService;

    @InjectMocks
    private VisaRuleMatcherBean underTest;

    @Test
    public void shouldReturnTrueWhenMatchingBusinessRule() {
        final VisaRecord visaRecord = new VisaRecord(VisaStatus.VALID, VisaType.createVisaType("test"), Collections.emptyList());
        final Optional<VisaTypeRule> visaTypeRuleOpt = Optional.of(new VisaTypeRule(VisaType.createVisaType("test"), Collections.emptyList()));

        when(mockVisaTypeService.findVisaTypeRule(visaRecord)).thenReturn(Tuple.tpl(visaTypeRuleOpt, Collections.emptyList()));

        assertTrue(underTest.hasVisaRule(visaRecord, (visaRules) -> {}));
    }

    @Test
    public void shouldReturnFalseWhenNotMatchingBusinessRule() {
        final VisaRecord visaRecord = new VisaRecord(VisaStatus.VALID, VisaType.createVisaType("test"), Collections.emptyList());
        final Optional<VisaTypeRule> visaTypeRuleOpt = Optional.empty();

        when(mockVisaTypeService.findVisaTypeRule(visaRecord)).thenReturn(Tuple.tpl(visaTypeRuleOpt, Collections.emptyList()));

        assertFalse(underTest.hasVisaRule(visaRecord, (visaRules) -> {}));
    }

}