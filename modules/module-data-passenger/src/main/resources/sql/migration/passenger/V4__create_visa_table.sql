CREATE TABLE visa
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  passport_number VARCHAR(200) NOT NULL,
  valid_from DATE,
  valid_to DATE,
  spx_number VARCHAR(200)
);