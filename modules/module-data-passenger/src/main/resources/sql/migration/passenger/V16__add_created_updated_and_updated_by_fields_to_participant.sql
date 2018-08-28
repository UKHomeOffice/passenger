ALTER TABLE participant ADD created TIMESTAMP DEFAULT NOW();
ALTER TABLE participant ADD last_updated TIMESTAMP;
ALTER TABLE participant ADD updated_by VARCHAR(250);
