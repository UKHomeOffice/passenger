package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleContentDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class FindAllVisaRuleContentByRuleTest {

    @Test
    public void shouldFindAllVisaRuleContents() {
        final String rule = "CODE_1";

        final Handle mockHandle = mock(Handle.class);
        final VisaRuleContentDAO mockDAO = mock(VisaRuleContentDAO.class);

        when(mockHandle.attach(VisaRuleContentDAO.class)).thenReturn(mockDAO);
        when(mockDAO.selectByRule(rule)).thenReturn(Collections.emptyList());

        final FindAllVisaRuleContentByRule underTest = new FindAllVisaRuleContentByRule(rule);
        underTest.withHandle(mockHandle);

        verify(mockDAO).selectByRule(rule);
    }

}