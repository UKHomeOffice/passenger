CREATE TABLE entry_clearance
(
  passport_number      VARCHAR(100) NOT NULL
    PRIMARY KEY,
  start_date           DATE         NOT NULL,
  end_date             DATE         NOT NULL,
  visa_valid_to_date   DATE         NOT NULL,
  tier_type            VARCHAR(100) NOT NULL,
  passport_nationality VARCHAR(200) NOT NULL,
  surname              VARCHAR(200) NOT NULL,
  other_names          VARCHAR(200) NULL,
  date_of_birth        DATE         NOT NULL,
  vaf_number           VARCHAR(200) NULL,
  cas_number           VARCHAR(200) NULL,
  spx_number           VARCHAR(200) NULL,
  conditions_1    VARCHAR(200) NULL,
  conditions_2    VARCHAR(200) NULL,
  CONSTRAINT entry_clearance_passport_number_uindex
  UNIQUE (passport_number)
);

