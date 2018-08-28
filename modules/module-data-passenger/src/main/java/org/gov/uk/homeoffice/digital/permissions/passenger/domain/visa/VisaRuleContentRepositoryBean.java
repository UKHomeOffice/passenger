package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.FindAllVisaRuleContentByRule;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class VisaRuleContentRepositoryBean implements VisaRuleContentRepository {

    private Jdbi jdbi;

    @Autowired
    public VisaRuleContentRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Collection<VisaRuleContent> findByRule(final String rule) {
        return jdbi.withHandle(new FindAllVisaRuleContentByRule(rule));
    }

}
