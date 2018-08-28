package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class FindAllOptionalVisaRulesTest {

    @Test
    public void shouldFindAllVisaRules() {
        final Handle mockHandle = mock(Handle.class);
        final VisaRuleLookupDAO mockDAO = mock(VisaRuleLookupDAO.class);

        when(mockHandle.attach(VisaRuleLookupDAO.class)).thenReturn(mockDAO);
        when(mockDAO.selectAll()).thenReturn(Collections.emptyList());

        final FindAllOptionalVisaRules underTest = new FindAllOptionalVisaRules();
        underTest.withHandle(mockHandle);

        verify(mockDAO).selectAllOptional();
    }

}