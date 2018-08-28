CREATE TABLE login_attempts
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  passport_number VARCHAR(200)  NULL,
  created TIMESTAMP DEFAULT  NOW(),
  success BOOLEAN NOT NULL
);

