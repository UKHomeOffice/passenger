package org.gov.uk.homeoffice.digital.permissions.passenger.visa.rule;

import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;

public interface DynamicContentProcessor {

    String transform(String input, Object... dataObjects);

    VisaRuleContent transform(VisaRuleContent visaRuleContent, Object... dataObjects);

}
