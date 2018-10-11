ALTER TABLE audit ADD COLUMN passenger_email VARCHAR (254) NULL;
ALTER TABLE audit ADD COLUMN passenger_passport_no VARCHAR (20) NULL;
ALTER TABLE audit ADD COLUMN passenger_name VARCHAR (254) NULL;

CREATE INDEX idx_pass_email ON audit (passenger_email);
CREATE INDEX idx_pass_passport_no ON audit (passenger_passport_no);
CREATE INDEX idx_pass_name ON audit (passenger_name);
CREATE INDEX idx_user_name ON audit (user_name);
