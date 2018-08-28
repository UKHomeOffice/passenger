package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class RemoveVisaRuleAction implements HandleConsumer<JdbiException> {

    private final String rule;

    public RemoveVisaRuleAction(final String rule) {
        this.rule = rule;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        handle.attach(VisaRuleLookupDAO.class).delete(rule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveVisaRuleAction that = (RemoveVisaRuleAction) o;
        return Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule);
    }

}
