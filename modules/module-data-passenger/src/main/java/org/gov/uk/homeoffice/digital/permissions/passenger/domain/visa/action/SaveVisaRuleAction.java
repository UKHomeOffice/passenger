package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.JdbiException;

import java.util.Objects;

public class SaveVisaRuleAction implements HandleConsumer<JdbiException> {

    private final VisaRule visaRule;

    public SaveVisaRuleAction(final VisaRule visaRule) {
        this.visaRule = visaRule;
    }

    @Override
    public void useHandle(final Handle handle) throws JdbiException {
        final VisaRuleLookupDAO dao = handle.attach(VisaRuleLookupDAO.class);
        dao.insert(visaRule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveVisaRuleAction that = (SaveVisaRuleAction) o;
        return Objects.equals(visaRule, that.visaRule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visaRule);
    }

}
