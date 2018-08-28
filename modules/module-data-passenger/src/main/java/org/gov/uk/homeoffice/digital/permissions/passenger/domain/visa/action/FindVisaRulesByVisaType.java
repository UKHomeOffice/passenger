package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;
import java.util.Objects;

public class FindVisaRulesByVisaType implements HandleCallback<Collection<VisaRule>, JdbiException> {

    private Long visaTypeId;

    public FindVisaRulesByVisaType(final Long visaTypeId) {
        this.visaTypeId = visaTypeId;
    }

    @Override
    public Collection<VisaRule> withHandle(final Handle handle) throws JdbiException {
        return handle.attach(VisaRuleLookupDAO.class).selectAllByVisaType(visaTypeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindVisaRulesByVisaType that = (FindVisaRulesByVisaType) o;
        return Objects.equals(visaTypeId, that.visaTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visaTypeId);
    }

}
