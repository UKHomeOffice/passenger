CREATE TABLE visa_type (
  id BIGSERIAL NOT NULL,
	name VARCHAR(255) NOT NULL,
	notes VARCHAR(255),
	enabled BOOLEAN DEFAULT TRUE,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE visa_rule_lookup (
	rule VARCHAR(50) NOT NULL,
	display_content VARCHAR(800) NULL,
	enabled BOOLEAN DEFAULT TRUE,
	editable BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (rule)
);

CREATE TABLE visa_type_rule_map (
	visa_type_id BIGINT NOT NULL,
	rule VARCHAR(50) NOT NULL,
	PRIMARY KEY (visa_type_id, rule),
	FOREIGN KEY (visa_type_id) REFERENCES visa_type (id) ON DELETE CASCADE,
	FOREIGN KEY (rule) REFERENCES visa_rule_lookup (rule)
);

INSERT INTO visa_type (id, name, notes) VALUES (1, 'Tier 4 (General) Student', 'Privately funded');
INSERT INTO visa_type (id, name, notes) VALUES (2, 'Tier 4 (General) Student', 'Studying more than 12 months at Degree level and above');
INSERT INTO visa_type (id, name, notes) VALUES (3, 'Tier 4 (General) Student', 'Studying a course below degree level');
INSERT INTO visa_type (id, name, notes) VALUES (4, 'Tier 4 (General) Student', 'Further Education - any NQF Level');
INSERT INTO visa_type (id, name, notes) VALUES (5, 'Tier 4 (General (S)) Student', 'Privately funded');
INSERT INTO visa_type (id, name, notes) VALUES (6, 'Tier 4 (General (S)) Student', 'Studying Degree level and above');
INSERT INTO visa_type (id, name, notes) VALUES (7, 'Tier 4 (General (S)) Student', 'Studying a course below degree level');
INSERT INTO visa_type (id, name, notes) VALUES (8, 'Tier 4 (General (S)) Student', 'Further Education - any NQF Level');
INSERT INTO visa_type (id, name, notes) VALUES (9, 'Tier 4 (General) Dependent Partner', '12+ Months Study  Degree level');
INSERT INTO visa_type (id, name, notes) VALUES (10, 'Tier 4 (General) Dependent Partner', '12- Months Study');
INSERT INTO visa_type (id, name, notes) VALUES (11, 'Tier 4 (General) Dependent Partner', 'Studying a course below degree level');
INSERT INTO visa_type (id, name, notes) VALUES (12, 'Tier 4 (General) Dependent Child', 'Studying more than 12 Months at Degreel level and above');
INSERT INTO visa_type (id, name, notes) VALUES (13, 'Tier 4 (General) Dependent Child', 'Studying less than 12 Months');
INSERT INTO visa_type (id, name, notes) VALUES (14, 'Tier 4 (General) Dependent Child', 'Studying less than 12 Months or below degree level');
INSERT INTO visa_type (id, name, notes) VALUES (15, 'Tier 4 (Child(S)) Student', '<16 yrs old');
INSERT INTO visa_type (id, name, notes) VALUES (16, 'Tier 4 (Child(S)) Student', '16+ yrs old');
INSERT INTO visa_type (id, name, notes) VALUES (17, 'Tier 4 (Child) Student', '16+');
INSERT INTO visa_type (id, name, notes) VALUES (18, 'Tier 4 (Child) Student', '16-');
INSERT INTO visa_type (id, name, notes) VALUES (19, 'TIER 5 (YOUTH MOB) MIGRANT', '');
INSERT INTO visa_type (id, name, notes) VALUES (20, 'TIER 5 TW (CHARITY) MIGRANT', '');
INSERT INTO visa_type (id, name, notes) VALUES (21, 'TIER 5 TW (CRE-SPORT) MIGRANT', '');
INSERT INTO visa_type (id, name, notes) VALUES (22, 'TIER 5 TW (EXCHANGE) MIGRANT', '');
INSERT INTO visa_type (id, name, notes) VALUES (23, 'TIER 5 TW (INT AGREE) MIGRANT', '');
INSERT INTO visa_type (id, name, notes) VALUES (24, 'TIER 5 TW (RELIGIOUS) MIGRANT', '');
INSERT INTO visa_type (id, name, notes) VALUES (25, 'TIER 5 TW PARTNER', '');
INSERT INTO visa_type (id, name, notes) VALUES (26, 'TIER 5 TW CHILD', '');

INSERT INTO visa_rule_lookup (rule) VALUES ('PLACE_OF_ISSUE');
INSERT INTO visa_rule_lookup (rule) VALUES ('VALID_FROM');
INSERT INTO visa_rule_lookup (rule) VALUES ('VALID_UNTIL');
INSERT INTO visa_rule_lookup (rule) VALUES ('NUMBER_OF_ENTRIES');
INSERT INTO visa_rule_lookup (rule) VALUES ('VAF_NUMBER');
INSERT INTO visa_rule_lookup (rule) VALUES ('VISA_TYPE');
INSERT INTO visa_rule_lookup (rule) VALUES ('NAME');
INSERT INTO visa_rule_lookup (rule) VALUES ('PASSPORT_NUMBER');
INSERT INTO visa_rule_lookup (rule) VALUES ('GENDER');
INSERT INTO visa_rule_lookup (rule) VALUES ('DATE_OF_BIRTH');
INSERT INTO visa_rule_lookup (rule) VALUES ('NATIONALITY');
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('CODE_1', 'On your current visa you cannot get public funds.', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('CODE_1A', 'On your current visa you have limited leave to enter the UK.', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('CODE_2', 'On your current visa if you work in the UK this must be authorised (and any changes) must be authorised.', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('CODE_3', 'On your current visa you cannot work or recourse to public funds.', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('CODE_4', 'On your current visa you cannot get public funds. You can work with * changes must be authorised * name or organisation/employer: add endorsement field.', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('CODE_7', 'On your current visa you cannot get public funds. You must leave the UK by *', TRUE);
INSERT INTO visa_rule_lookup (rule) VALUES ('ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_rule_lookup (rule) VALUES ('COS_NUMBER');
INSERT INTO visa_rule_lookup (rule) VALUES ('CAS_NUMBER');
INSERT INTO visa_rule_lookup (rule) VALUES ('SPX_NUMBER');
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('10_HOURS', 'Part time (up to 10 hours a week) during term time', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('20_HOURS', 'Part time (up to 20 hours a week) during term time', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('SPORTS', 'No employment as a professional sportsperson (including as a sports coach).', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('DOCTOR', 'Cannot work as a doctor or dentist in training – except on a recognised foundation course', TRUE);
INSERT INTO visa_rule_lookup (rule, display_content, editable) VALUES ('BUSINESS', 'On your current visa you cannot engage in business activity, such as being self-employed or running a business', TRUE);
INSERT INTO visa_rule_lookup (rule) VALUES ('POLICE_REGISTRATION_NVN');
INSERT INTO visa_rule_lookup (rule) VALUES ('POLICE_REGISTRATION_VN');
INSERT INTO visa_rule_lookup (rule) VALUES ('FULL_NAME');
INSERT INTO visa_rule_lookup (rule) VALUES ('SURNAME');
INSERT INTO visa_rule_lookup (rule) VALUES ('REASON');
INSERT INTO visa_rule_lookup (rule) VALUES ('WORK_UNTIL');

-- tier 4

INSERT INTO visa_type_rule_map VALUES (1, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (1, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (1, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (1, 'NAME');
INSERT INTO visa_type_rule_map VALUES (1, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (1, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (1, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (1, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (1, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (1, 'SPX_NUMBER');

INSERT INTO visa_type_rule_map VALUES (2, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (2, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (2, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (2, 'NAME');
INSERT INTO visa_type_rule_map VALUES (2, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (2, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (2, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (2, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (2, 'CODE_2');
INSERT INTO visa_type_rule_map VALUES (2, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (2, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (2, '20_HOURS');

INSERT INTO visa_type_rule_map VALUES (3, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (3, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (3, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (3, 'NAME');
INSERT INTO visa_type_rule_map VALUES (3, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (3, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (3, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (3, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (3, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (3, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (3, '10_HOURS');
INSERT INTO visa_type_rule_map VALUES (3, 'POLICE_REGISTRATION_VN');

INSERT INTO visa_type_rule_map VALUES (4, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (4, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (4, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (4, 'NAME');
INSERT INTO visa_type_rule_map VALUES (4, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (4, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (4, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (4, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (4, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (4, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (4, '10_HOURS');

INSERT INTO visa_type_rule_map VALUES (5, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (5, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (5, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (5, 'NAME');
INSERT INTO visa_type_rule_map VALUES (5, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (5, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (5, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (5, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (5, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (5, 'SPX_NUMBER');

INSERT INTO visa_type_rule_map VALUES (6, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (6, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (6, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (6, 'NAME');
INSERT INTO visa_type_rule_map VALUES (6, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (6, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (6, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (6, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (6, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (6, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (6, '20_HOURS');

INSERT INTO visa_type_rule_map VALUES (7, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (7, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (7, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (7, 'NAME');
INSERT INTO visa_type_rule_map VALUES (7, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (7, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (7, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (7, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (7, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (7, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (7, '10_HOURS');

INSERT INTO visa_type_rule_map VALUES (8, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (8, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (8, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (8, 'NAME');
INSERT INTO visa_type_rule_map VALUES (8, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (8, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (8, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (8, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (8, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (8, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (8, '10_HOURS');

INSERT INTO visa_type_rule_map VALUES (9, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (9, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (9, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (9, 'NAME');
INSERT INTO visa_type_rule_map VALUES (9, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (9, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (9, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (9, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (9, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (9, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (9, 'BUSINESS');
INSERT INTO visa_type_rule_map VALUES (9, 'DOCTOR');

INSERT INTO visa_type_rule_map VALUES (10, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (10, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (10, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (10, 'NAME');
INSERT INTO visa_type_rule_map VALUES (10, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (10, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (10, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (10, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (10, 'ADDITIONAL_ENDORSEMENT');

INSERT INTO visa_type_rule_map VALUES (11, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (11, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (11, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (11, 'NAME');
INSERT INTO visa_type_rule_map VALUES (11, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (11, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (11, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (11, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (11, 'ADDITIONAL_ENDORSEMENT');

INSERT INTO visa_type_rule_map VALUES (12, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (12, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (12, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (12, 'NAME');
INSERT INTO visa_type_rule_map VALUES (12, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (12, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (12, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (12, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (12, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (12, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (12, 'BUSINESS');
INSERT INTO visa_type_rule_map VALUES (12, 'DOCTOR');

INSERT INTO visa_type_rule_map VALUES (13, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (13, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (13, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (13, 'NAME');
INSERT INTO visa_type_rule_map VALUES (13, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (13, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (13, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (13, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (13, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (13, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (13, 'BUSINESS');
INSERT INTO visa_type_rule_map VALUES (13, 'DOCTOR');

INSERT INTO visa_type_rule_map VALUES (14, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (14, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (14, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (14, 'NAME');
INSERT INTO visa_type_rule_map VALUES (14, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (14, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (14, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (14, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (14, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (14, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (14, 'BUSINESS');
INSERT INTO visa_type_rule_map VALUES (14, 'DOCTOR');

INSERT INTO visa_type_rule_map VALUES (15, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (15, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (15, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (15, 'NAME');
INSERT INTO visa_type_rule_map VALUES (15, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (15, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (15, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (15, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (15, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (15, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (15, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (15, 'ADDITIONAL_ENDORSEMENT');

INSERT INTO visa_type_rule_map VALUES (16, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (16, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (16, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (16, 'NAME');
INSERT INTO visa_type_rule_map VALUES (16, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (16, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (16, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (16, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (16, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (16, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (16, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (16, '10_HOURS');

INSERT INTO visa_type_rule_map VALUES (17, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (17, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (17, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (17, 'NAME');
INSERT INTO visa_type_rule_map VALUES (17, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (17, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (17, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (17, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (17, 'CODE_3');
INSERT INTO visa_type_rule_map VALUES (17, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (17, 'ADDITIONAL_ENDORSEMENT');

INSERT INTO visa_type_rule_map VALUES (18, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (18, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (18, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (18, 'NAME');
INSERT INTO visa_type_rule_map VALUES (18, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (18, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (18, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (18, 'CODE_2');
INSERT INTO visa_type_rule_map VALUES (18, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (18, 'CAS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (18, 'SPX_NUMBER');
INSERT INTO visa_type_rule_map VALUES (18, '10_HOURS');

-- tier 5

INSERT INTO visa_type_rule_map VALUES (19, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (19, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (19, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (19, 'NAME');
INSERT INTO visa_type_rule_map VALUES (19, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (19, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (19, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (19, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (19, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (19, 'BUSINESS');
INSERT INTO visa_type_rule_map VALUES (19, 'DOCTOR');
INSERT INTO visa_type_rule_map VALUES (19, 'WORK_UNTIL');

INSERT INTO visa_type_rule_map VALUES (20, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (20, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (20, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (20, 'NAME');
INSERT INTO visa_type_rule_map VALUES (20, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (20, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (20, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (20, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (20, 'COS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (20, 'DOCTOR');
INSERT INTO visa_type_rule_map VALUES (20, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (20, 'WORK_UNTIL');

INSERT INTO visa_type_rule_map VALUES (21, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (21, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (21, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (21, 'NAME');
INSERT INTO visa_type_rule_map VALUES (21, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (21, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (21, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (21, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (21, 'COS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (21, 'DOCTOR');
INSERT INTO visa_type_rule_map VALUES (21, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (21, 'WORK_UNTIL');

INSERT INTO visa_type_rule_map VALUES (22, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (22, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (22, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (22, 'NAME');
INSERT INTO visa_type_rule_map VALUES (22, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (22, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (22, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (22, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (22, 'COS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (22, 'DOCTOR');
INSERT INTO visa_type_rule_map VALUES (22, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (22, 'WORK_UNTIL');

INSERT INTO visa_type_rule_map VALUES (23, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (23, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (23, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (23, 'NAME');
INSERT INTO visa_type_rule_map VALUES (23, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (23, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (23, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (23, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (23, 'COS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (23, 'DOCTOR');
INSERT INTO visa_type_rule_map VALUES (23, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (23, 'WORK_UNTIL');

INSERT INTO visa_type_rule_map VALUES (24, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (24, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (24, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (24, 'NAME');
INSERT INTO visa_type_rule_map VALUES (24, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (24, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (24, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (24, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (24, 'COS_NUMBER');
INSERT INTO visa_type_rule_map VALUES (24, 'DOCTOR');
INSERT INTO visa_type_rule_map VALUES (24, 'SPORTS');
INSERT INTO visa_type_rule_map VALUES (24, 'WORK_UNTIL');

INSERT INTO visa_type_rule_map VALUES (25, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (25, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (25, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (25, 'NAME');
INSERT INTO visa_type_rule_map VALUES (25, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (25, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (25, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (25, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (25, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (25, 'DOCTOR');

INSERT INTO visa_type_rule_map VALUES (26, 'VALID_FROM');
INSERT INTO visa_type_rule_map VALUES (26, 'VALID_UNTIL');
INSERT INTO visa_type_rule_map VALUES (26, 'VISA_TYPE');
INSERT INTO visa_type_rule_map VALUES (26, 'NAME');
INSERT INTO visa_type_rule_map VALUES (26, 'PASSPORT_NUMBER');
INSERT INTO visa_type_rule_map VALUES (26, 'DATE_OF_BIRTH');
INSERT INTO visa_type_rule_map VALUES (26, 'NATIONALITY');
INSERT INTO visa_type_rule_map VALUES (26, 'CODE_1');
INSERT INTO visa_type_rule_map VALUES (26, 'ADDITIONAL_ENDORSEMENT');
INSERT INTO visa_type_rule_map VALUES (26, 'SPORTS');