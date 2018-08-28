package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class RemoveVisaRuleActionTest {

    @Test
    public void shouldRemoveVisaRule() {
        final Handle mockHandle = mock(Handle.class);
        final VisaRuleLookupDAO mockDAO = mock(VisaRuleLookupDAO.class);

        when(mockHandle.attach(VisaRuleLookupDAO.class)).thenReturn(mockDAO);

        final RemoveVisaRuleAction underTest = new RemoveVisaRuleAction("rule");
        underTest.useHandle(mockHandle);

        verify(mockDAO).delete("rule");
    }

}