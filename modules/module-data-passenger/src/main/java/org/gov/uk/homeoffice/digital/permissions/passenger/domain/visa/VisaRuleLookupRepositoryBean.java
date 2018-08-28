package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa.action.*;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class VisaRuleLookupRepositoryBean implements VisaRuleLookupRepository {

    private Jdbi jdbi;

    @Autowired
    public VisaRuleLookupRepositoryBean(@Qualifier("passenger.db") final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public VisaRule save(final VisaRule visaRule) {
        jdbi.useTransaction(new SaveVisaRuleAction(visaRule));
        return visaRule;
    }

    @Override
    public void remove(final VisaRule visaRule) {
        remove(visaRule.getRule());
    }

    @Override
    public void remove(final String rule) {
        jdbi.useTransaction(new RemoveVisaRuleAction(rule));
    }

    @Override
    public Optional<VisaRule> findOneByRule(final String rule) {
        return jdbi.withHandle(new FindVisaRuleByRule(rule));
    }

    @Override
    public Collection<VisaRule> findAll() {
        return jdbi.withHandle(new FindAllVisaRules());
    }


    @Override
    public Collection<VisaRule> findAllOptional() {
        return jdbi.withHandle(new FindAllOptionalVisaRules());
    }

    @Override
    public Collection<VisaRule> findByVisaType(final Long visaTypeId) {
        return jdbi.withHandle(new FindVisaRulesByVisaType(visaTypeId));
    }

}
