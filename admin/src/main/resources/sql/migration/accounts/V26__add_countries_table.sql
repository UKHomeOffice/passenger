CREATE TABLE countries (
  iso_country_code CHAR(2) NOT NULL,
  enabled BOOLEAN DEFAULT FALSE,
  created TIMESTAMP DEFAULT NOW(),
  updated TIMESTAMP DEFAULT NOW(),
  PRIMARY KEY (iso_country_code)
);