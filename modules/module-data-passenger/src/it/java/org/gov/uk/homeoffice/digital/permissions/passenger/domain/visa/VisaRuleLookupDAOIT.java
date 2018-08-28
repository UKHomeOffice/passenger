package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.PassengerDBITConfiguration;
import org.gov.uk.homeoffice.digital.permissions.passenger.TruncateTablesBeforeEachTest;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PassengerDBITConfiguration.class)
@TruncateTablesBeforeEachTest
public class VisaRuleLookupDAOIT {

    @Autowired
    @Qualifier("passenger.db")
    private Jdbi dbi;

    @Test
    public void insertAndGetVisaRule() {
        final VisaRule visaRule = new VisaRule("INSERT_TEST_RULE");
        dbi.useHandle(handle -> {
            final VisaRuleLookupDAO dao = handle.attach(VisaRuleLookupDAO.class);
            dao.insert(visaRule);
            assertTrue(dao.selectByRule("INSERT_TEST_RULE").isPresent());
        });
    }

    @Test
    public void deleteAndGetVisaRule() {
        final VisaRule visaRule = new VisaRule("INSERT_TEST_RULE");
        dbi.useHandle(handle -> {
            final VisaRuleLookupDAO dao = handle.attach(VisaRuleLookupDAO.class);
            dao.insert(visaRule);
            dao.delete(visaRule);
            assertFalse(dao.selectByRule("INSERT_TEST_RULE").isPresent());
        });
    }

    @Test
    public void deleteByNameAndGetVisaRule() {
        final VisaRule visaRule = new VisaRule("INSERT_TEST_RULE");
        dbi.useHandle(handle -> {
            final VisaRuleLookupDAO dao = handle.attach(VisaRuleLookupDAO.class);
            dao.insert(visaRule);
            dao.delete("INSERT_TEST_RULE");
            assertFalse(dao.selectByRule("INSERT_TEST_RULE").isPresent());
        });
    }

    @Test
    public void selectAllVisaRules() {
        final VisaRule visaRule1 = new VisaRule("INSERT_TEST_RULE");
        final VisaRule visaRule2 = new VisaRule("INSERT_TEST_RULE_2");
        dbi.useHandle(handle -> {
            final VisaRuleLookupDAO dao = handle.attach(VisaRuleLookupDAO.class);
            dao.insert(visaRule1);
            dao.insert(visaRule2);
            Collection<VisaRule> allRules = dao.selectAll();
            assertThat(allRules.size(), is(2));
        });
    }

    @Test
    public void selectAllEnabledVisaRules() {
        final VisaRule visaRule1 = new VisaRule("INSERT_TEST_RULE");
        final VisaRule visaRule2 = new VisaRule("INSERT_TEST_RULE_2", false);
        dbi.useHandle(handle -> {
            final VisaRuleLookupDAO dao = handle.attach(VisaRuleLookupDAO.class);
            dao.insert(visaRule1);
            dao.insert(visaRule2);
            Collection<VisaRule> allRules = dao.selectAllEnabled();
            assertThat(allRules.size(), is(1));
        });
    }

}