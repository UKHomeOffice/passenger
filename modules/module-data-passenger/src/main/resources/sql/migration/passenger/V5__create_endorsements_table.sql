CREATE TABLE endorsements
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  visa_id BIGINT NOT NULL,
  value VARCHAR(1000) NOT NULL,
  CONSTRAINT endorsements_visa_id_fk FOREIGN KEY (visa_id) REFERENCES visa (id)
);