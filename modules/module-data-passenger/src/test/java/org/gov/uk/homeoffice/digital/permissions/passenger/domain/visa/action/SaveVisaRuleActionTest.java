package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SaveVisaRuleActionTest {

    @Test
    public void shouldInsertVisaRule() {
        final Handle mockHandle = mock(Handle.class);
        final VisaRuleLookupDAO mockDAO = mock(VisaRuleLookupDAO.class);

        when(mockHandle.attach(VisaRuleLookupDAO.class)).thenReturn(mockDAO);
        when(mockDAO.selectByRule("rule")).thenReturn(Optional.empty());

        final SaveVisaRuleAction underTest = new SaveVisaRuleAction(new VisaRule("rule"));
        underTest.useHandle(mockHandle);

        verify(mockDAO).insert(new VisaRule("rule"));
    }

}