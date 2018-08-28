package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;

import java.util.Collection;
import java.util.Optional;

public interface VisaRuleLookupRepository {

    VisaRule save(VisaRule visaRule);

    void remove(VisaRule visaRule);

    void remove(String rule);

    Optional<VisaRule> findOneByRule(String rule);

    Collection<VisaRule> findByVisaType(Long visaTypeId);

    Collection<VisaRule> findAll();

    Collection<VisaRule> findAllOptional();

}
