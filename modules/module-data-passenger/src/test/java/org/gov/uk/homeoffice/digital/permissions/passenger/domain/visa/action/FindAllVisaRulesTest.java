package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;

public class FindAllVisaRulesTest {

    @Test
    public void shouldFindAllVisaRules() {
        final Handle mockHandle = mock(Handle.class);
        final VisaRuleLookupDAO mockDAO = mock(VisaRuleLookupDAO.class);

        when(mockHandle.attach(VisaRuleLookupDAO.class)).thenReturn(mockDAO);
        when(mockDAO.selectAll()).thenReturn(Collections.emptyList());

        final FindAllVisaRules underTest = new FindAllVisaRules();
        underTest.withHandle(mockHandle);

        verify(mockDAO).selectAll();
    }

    /*@Test
    public void equalsTest() {
        FindAllVisaRules x = new FindAllVisaRules();
        FindAllVisaRules y = new FindAllVisaRules();
        FindAllVisaRules z = new FindAllVisaRules();
        Integer n = 123;

        assertThat(x.equals(x), is(true)); // reflexive
        assertThat(x.equals(y), is(true)); // symmetric
        assertThat(y.equals(x), is(true));
        assertThat(y.equals(z), is(true)); // transitive
        assertThat(x.equals(z), is(true));

        assertThat(x.equals(n), is(false));
        assertThat(x.equals(null), is(false));
    }

    @Test
    public void hashCodeTest() {
        FindAllVisaRules a = new FindAllVisaRules();
        FindAllVisaRules b = new FindAllVisaRules();
        Integer n = 123;

        assertThat(a.hashCode(), is(b.hashCode()));
        assertThat(a.hashCode(), is(not(n.hashCode())));
    }*/

}