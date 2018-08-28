package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.VisaRuleLookupDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.JdbiException;

import java.util.Collection;

public class FindAllOptionalVisaRules implements HandleCallback<Collection<VisaRule>, JdbiException> {

    @Override
    public Collection<VisaRule> withHandle(Handle handle) throws JdbiException {
        return handle.attach(VisaRuleLookupDAO.class).selectAllOptional();
    }

}
