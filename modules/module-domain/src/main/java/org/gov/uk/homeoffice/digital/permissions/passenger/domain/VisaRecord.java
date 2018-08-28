package org.gov.uk.homeoffice.digital.permissions.passenger.domain;

import org.gov.uk.homeoffice.digital.permissions.passenger.utils.Tuple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class VisaRecord {

    private VisaStatus visaStatus;
    private Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> visaRulesMapping;
    private VisaType visaType;

    public VisaRecord(final VisaStatus visaStatus,
                      final VisaType visaType,
                      final Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> visaRulesMapping) {
        this.visaStatus = visaStatus;
        this.visaType = visaType;
        this.visaRulesMapping = visaRulesMapping;
    }

    public Collection<Tuple<VisaRule, Collection<VisaRuleContent>>> getVisaRulesMapping() {
        return visaRulesMapping;
    }

    public Collection<VisaRule> getVisaRules() {
        return visaRulesMapping.stream().map(Tuple::get_1).collect(Collectors.toList());
    }

    public VisaType getVisaType() {
        return visaType;
    }

    public VisaStatus getVisaStatus() {
        return visaStatus;
    }

    public Collection<VisaRuleContent> valuesFor(final String ruleName) {
        return getVisaRulesMapping().stream()
                .filter(tpl -> tpl.get_1().getRule().equalsIgnoreCase(ruleName))
                .map(Tuple::get_2)
                .findFirst()
                .orElse(null);
    }

    public Optional<VisaRuleContent> firstValueFor(final String ruleName) {
        return valuesFor(ruleName).stream().findFirst();
    }

    public String firstValueAsStringFor(final String ruleName) {
        return valuesFor(ruleName).stream().findFirst().map(VisaRuleContent::getContent).orElse("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisaRecord that = (VisaRecord) o;
        return visaStatus == that.visaStatus &&
                Objects.equals(visaRulesMapping, that.visaRulesMapping) &&
                Objects.equals(visaType, that.visaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visaStatus, visaRulesMapping, visaType);
    }

    @Override
    public String toString() {
        return "VisaRecord{\n" +
                "    visaStatus=" + visaStatus +
                ",\n    visaType=" + visaType +
                ",\n    visaRulesMapping=[\n" + visaRulesMapping.stream().sorted(Comparator.comparing(t -> t.get_1().getRule())).map(t -> t.toString()).collect(Collectors.joining(",\n")) +
                "\n]}";
    }
}
