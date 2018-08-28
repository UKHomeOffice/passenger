package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;

import java.util.Collection;

public interface VisaRuleContentRepository {

    Collection<VisaRuleContent> findByRule(String rule);

}
