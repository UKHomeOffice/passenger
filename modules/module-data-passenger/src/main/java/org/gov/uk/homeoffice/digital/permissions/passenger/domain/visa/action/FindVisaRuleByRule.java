package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;
import java.util.Optional;

public class FindVisaRuleByRule implements HandleCallback<Optional<VisaRule>, JdbiException> {

    private final String rule;

    public FindVisaRuleByRule(final String rule) {
        this.rule = rule;
    }

    @Override
    public Optional<VisaRule> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(VisaRuleLookupDAO.class).selectByRule(rule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindVisaRuleByRule that = (FindVisaRuleByRule) o;
        return Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule);
    }

}
