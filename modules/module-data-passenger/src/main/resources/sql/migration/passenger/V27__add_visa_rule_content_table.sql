CREATE TABLE rule_type (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  rule_type VARCHAR(10) NOT NULL
);

CREATE TABLE visa_rule_content
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  rule VARCHAR(50) NOT NULL,
  content VARCHAR (1000) NOT NULL,
  enabled BOOLEAN DEFAULT TRUE,
  rule_type VARCHAR(10) NOT NULL,
  CONSTRAINT vrc_rule_fk FOREIGN KEY (rule) REFERENCES visa_rule_lookup (rule)
);

INSERT INTO rule_type (id, rule_type) VALUES (1, 'POSITIVE');
INSERT INTO rule_type (id, rule_type) VALUES (2, 'NEGATIVE');
INSERT INTO rule_type (id, rule_type) VALUES (3, 'NOTE');

INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (1, 'CODE_1', 'Get access to public funds.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (2, 'CODE_1A', 'You have limited leave to enter the UK.', 'NOTE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (3, 'CODE_2', 'If you work in the UK this must be authorised (and any changes) must be authorised.', 'NOTE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (4, 'CODE_3', 'Work or recourse to public funds.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (5, 'CODE_4', 'Get access to public funds.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (6, 'CODE_4', 'Work with *.', 'POSITIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (7, 'CODE_4', 'Changes must be authorised.', 'NOTE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (8, 'CODE_7', 'Get access to public funds.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (9, 'CODE_7', 'You must leave the UK by *.', 'NOTE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (10, '10_HOURS', 'Work part time (up to 10 hours a week) during term time.', 'POSITIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (11, '10_HOURS', 'Work full time in the holidays.', 'POSITIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (12, '10_HOURS', 'Work as a doctor or dentist in training – except on a recognised foundation course.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (13, '10_HOURS', 'Work as an entertainer.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (14, '10_HOURS', 'Play or coach professional sports.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (15, '10_HOURS', 'Engage in business activity, such as being self-employed or running a business.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (16, '10_HOURS', 'You will need to show an employer proof of your term dates, for example a letter from your college or university.', 'NOTE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (17, '20_HOURS', 'Work part time (up to 20 hours a week) during term time.', 'POSITIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (18, '20_HOURS', 'Work full time in the holidays.', 'POSITIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (19, '20_HOURS', 'Work as a doctor or dentist in training – except on a recognised foundation course.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (20, '20_HOURS', 'Work as an entertainer.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (21, '20_HOURS', 'Play or coach professional sports.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (22, '20_HOURS', 'Engage in business activity, such as being self-employed or running a business.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (23, '20_HOURS', 'You will need to show an employer proof of your term dates, for example a letter from your college or university.', 'NOTE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (24, 'SPORTS', 'Play or coach professional sports.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (25, 'SPORTS', 'Work as an entertainer.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (26, 'BUSINESS', 'Engage in business activity, such as being self-employed or running a business.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (27, 'DOCTOR', 'Work as a doctor or dentist in training.', 'NEGATIVE');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (28, 'POLICE_REGISTRATION_NVN', 'You must register with the Police within 7 days of arrival.', 'USER_DATA');
INSERT INTO visa_rule_content (id, rule, content, rule_type) VALUES (29, 'POLICE_REGISTRATION_VN', 'You must register with the Police within 7 days of arrival.', 'USER_DATA');

ALTER TABLE visa_rule_lookup DROP COLUMN display_content;
ALTER TABLE visa_rule_lookup DROP COLUMN editable;
