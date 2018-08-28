CREATE TABLE accounts
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  email VARCHAR(200) NOT NULL,
  password VARCHAR(1000),
  uuid VARCHAR(40),
  created TIMESTAMP DEFAULT  NOW(),
  enabled BOOLEAN DEFAULT false
);
CREATE UNIQUE INDEX accounts_email_uindex ON accounts (email);
