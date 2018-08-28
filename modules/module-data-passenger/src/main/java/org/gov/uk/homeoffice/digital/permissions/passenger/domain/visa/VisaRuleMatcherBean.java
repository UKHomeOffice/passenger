package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRecord;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaTypeRule;
import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class VisaRuleMatcherBean implements VisaRuleMatcher {

    private VisaTypeServiceBean visaTypeService;

    @Autowired
    public VisaRuleMatcherBean(final VisaTypeServiceBean visaTypeService) {
        this.visaTypeService = visaTypeService;
    }

    @Override
    public boolean hasVisaRule(VisaRecord visaRecord, Consumer<List<String>> onFailureAction) {
        final Tuple<Optional<VisaTypeRule>, List<String>> matchingVisaTypeRules = visaTypeService.findVisaTypeRule(visaRecord);

        if (!matchingVisaTypeRules.get_1().isPresent()) {
            onFailureAction.accept(matchingVisaTypeRules.get_2());
            return false;
        }

        return true;
    }

}
