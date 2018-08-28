package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class VisaRuleTest {

    @Test
    public void shouldReturnRuleAndEnabled() {
        VisaRule rule = new VisaRule("RULE",true);
        assertThat(rule.getRule(), is("RULE"));
        assertThat(rule.getEnabled(), is(true));
    }

    @Test
    public void shouldBeEqualWithSameValues() {
        VisaRule rule1 = new VisaRule("RULE", true);
        VisaRule rule2 = new VisaRule("RULE", true);
        assertEquals(rule1, rule2);
    }

    @Test
    public void shouldNotBeEqualWithDifferentValues() {
        VisaRule rule1 = new VisaRule("RULE", false);
        VisaRule rule2 = new VisaRule("RULE", true);
        assertNotEquals(rule1, rule2);
    }

}