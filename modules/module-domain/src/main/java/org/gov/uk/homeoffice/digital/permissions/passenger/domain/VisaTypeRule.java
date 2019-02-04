package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@EqualsAndHashCode
public class VisaTypeRule implements Serializable {

    private VisaType visaType;
    private final Collection<VisaRule> visaRules;

    public VisaTypeRule() {
        this(null, null);
    }

    public VisaTypeRule(final VisaType visaType, final Collection<VisaRule> visaRules) {
        this.visaType = visaType;
        this.visaRules = (visaRules == null) ? Collections.emptyList() : visaRules;
    }

    public void setVisaType(final VisaType visaType) {
        this.visaType = visaType;
    }

    public void addVisaRules(final Collection<VisaRule> visaRules) {
        this.visaRules.addAll(visaRules == null ? Collections.emptyList() : visaRules);
    }

    public boolean hasVisaRule(final String visaRuleName) {
        return visaRuleName != null && visaRules.stream().anyMatch(visaRule -> visaRule.getRule().equals(visaRuleName));
    }

    public String getVisaType() {
        if(!isEmpty(visaType.getDescription())){
           return visaType.getDescription();
        }
        return visaType.getName();
    }

}
