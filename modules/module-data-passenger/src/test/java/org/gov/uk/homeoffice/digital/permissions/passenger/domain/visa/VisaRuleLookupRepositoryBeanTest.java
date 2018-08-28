package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.*;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VisaRuleLookupRepositoryBeanTest {

    private VisaRule visaRule = new VisaRule("rule");

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private VisaRuleLookupRepositoryBean visaRuleLookupRepository;

    @Test
    public void shouldSaveVisaRule() {
        visaRuleLookupRepository.save(visaRule);
        verify(mockJdbi).useTransaction(new SaveVisaRuleAction(visaRule));
    }

    @Test
    public void shouldRemoveVisaRule() {
        visaRuleLookupRepository.remove(visaRule.getRule());
        verify(mockJdbi).useTransaction(new RemoveVisaRuleAction(visaRule.getRule()));
    }

    @Test
    public void shouldFindVisaRuleByRuleName() {
        visaRuleLookupRepository.findOneByRule(visaRule.getRule());
        verify(mockJdbi).withHandle(new FindVisaRuleByRule(visaRule.getRule()));
    }

    @Test
    public void shouldFindAllVisaRules() {
        visaRuleLookupRepository.findAll();
        verify(mockJdbi).withHandle(any(FindAllVisaRules.class));
    }

    @Test
    public void shouldFindVisaRulesByVisaType() {
        visaRuleLookupRepository.findByVisaType(1L);
        verify(mockJdbi).withHandle(new FindVisaRulesByVisaType(1L));
    }

}