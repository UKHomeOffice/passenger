package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisaRuleContent implements Serializable {

    private Long id;
    private String rule;
    private String content;
    private Boolean enabled;
    private RuleType ruleType;

}
