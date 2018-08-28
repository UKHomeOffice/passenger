ALTER TABLE visa_rule_lookup ADD  optional BOOLEAN DEFAULT false ;
UPDATE visa_rule_lookup SET optional = true WHERE rule like 'POLICE%';