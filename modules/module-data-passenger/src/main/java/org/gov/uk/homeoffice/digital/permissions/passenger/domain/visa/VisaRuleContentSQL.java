package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

final class VisaRuleContentSQL {

    static final String SELECT_BY_RULE = "SELECT * FROM visa_rule_content WHERE rule = :rule AND enabled IS TRUE";

}
