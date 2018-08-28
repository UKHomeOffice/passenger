package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FindVisaRulesByVisaTypeTest {

    @Test
    public void shouldFindVisaRulesByVisaType() {
        final Handle mockHandle = mock(Handle.class);
        final VisaRuleLookupDAO mockDAO = mock(VisaRuleLookupDAO.class);

        when(mockHandle.attach(VisaRuleLookupDAO.class)).thenReturn(mockDAO);

        final FindVisaRulesByVisaType underTest = new FindVisaRulesByVisaType(1L);
        underTest.withHandle(mockHandle);

        verify(mockDAO).selectAllByVisaType(1L);
    }

}