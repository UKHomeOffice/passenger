package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.FindAllVisaRuleContentByRule;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VisaRuleContentRepositoryBeanTest {

    @Mock
    private Jdbi mockJdbi;

    @InjectMocks
    private VisaRuleContentRepositoryBean underTest;

    @Test
    public void shouldFindByRule() {
        underTest.findByRule("CODE_1");
        verify(mockJdbi).withHandle(new FindAllVisaRuleContentByRule("CODE_1"));
    }

}