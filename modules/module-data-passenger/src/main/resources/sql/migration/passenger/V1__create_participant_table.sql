CREATE TABLE participant
(
  id              BIGINT       NOT NULL
    PRIMARY KEY,
  surname         VARCHAR(200) NOT NULL,
  gender          VARCHAR(10)  NULL,
  date_of_birth   DATE         NOT NULL,
  passport_number VARCHAR(100) NOT NULL,
  mobile_number   VARCHAR(100) NULL,
  email           VARCHAR(100) NOT NULL,
  acl             VARCHAR(200) NULL,
  acl_address     VARCHAR(200) NULL,
  cas_number      VARCHAR(200) NULL,
  spx_number      VARCHAR(200) NULL,
  first_name      VARCHAR(200) NULL,
  valid_from      DATE NULL,
  condition_1     VARCHAR(200) NULL,
  condition_2     VARCHAR(200) NULL,
  stage           VARCHAR(10) NULL,

  CONSTRAINT participant_id_uindex
  UNIQUE (id),
  CONSTRAINT participant_passport_number_uindex
  UNIQUE (passport_number)
);

