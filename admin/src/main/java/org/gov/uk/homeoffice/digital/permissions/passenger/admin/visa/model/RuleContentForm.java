package org.gov.uk.homeoffice.digital.permissions.passenger.admin.visa.model;

import lombok.Data;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRule;

import java.util.List;

@Data
public class RuleContentForm {

    private List<VisaRule> visaRules;

}
