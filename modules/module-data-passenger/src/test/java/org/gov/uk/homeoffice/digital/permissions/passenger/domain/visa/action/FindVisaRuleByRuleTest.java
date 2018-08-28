package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class FindVisaRuleByRuleTest {

    @Test
    public void shouldFindRuleByRuleName() {
        final Handle mockHandle = mock(Handle.class);
        final VisaRuleLookupDAO mockDAO = mock(VisaRuleLookupDAO.class);

        when(mockHandle.attach(VisaRuleLookupDAO.class)).thenReturn(mockDAO);
        when(mockDAO.selectByRule("rule")).thenReturn(Optional.of(new VisaRule("rule")));

        final FindVisaRuleByRule underTest = new FindVisaRuleByRule("rule");
        underTest.withHandle(mockHandle);

        verify(mockDAO).selectByRule("rule");
    }

}