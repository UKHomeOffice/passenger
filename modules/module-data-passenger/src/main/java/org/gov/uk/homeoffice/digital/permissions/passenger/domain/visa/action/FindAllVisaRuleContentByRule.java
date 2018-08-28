package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleContentDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;
import java.util.Objects;

public class FindAllVisaRuleContentByRule implements HandleCallback<Collection<VisaRuleContent>, JdbiException> {

    private String rule;

    public FindAllVisaRuleContentByRule(final String rule) {
        this.rule = rule;
    }

    @Override
    public Collection<VisaRuleContent> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(VisaRuleContentDAO.class).selectByRule(rule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindAllVisaRuleContentByRule that = (FindAllVisaRuleContentByRule) o;
        return Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule);
    }

}
