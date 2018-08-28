package org.gov.uk.homeoffice.digital.permissions.passenger.domain.visa;

final class VisaRuleLookupSQL {

    static final String INSERT = "INSERT INTO visa_rule_lookup (rule, enabled) VALUES (:rule, :enabled)";
    static final String DELETE = "DELETE FROM visa_rule_lookup WHERE rule = :rule";
    static final String SELECT_BY_RULE = "SELECT * FROM visa_rule_lookup WHERE rule = :rule";
    static final String SELECT_ALL = "SELECT * FROM visa_rule_lookup";
    static final String SELECT_ALL_ENABLED = "SELECT * FROM visa_rule_lookup WHERE enabled IS TRUE";
    static final String SELECT_ALL_OPTIONAL = "SELECT * FROM visa_rule_lookup WHERE optional IS TRUE";
    static final String SELECT_ALL_BY_VISA_TYPE = "SELECT vrl.* FROM visa_rule_lookup vrl INNER JOIN visa_type_rule_map vtrm ON vrl.rule = vtrm.rule WHERE vtrm.visa_type_id = :visaTypeId AND vrl.enabled IS TRUE";

}
