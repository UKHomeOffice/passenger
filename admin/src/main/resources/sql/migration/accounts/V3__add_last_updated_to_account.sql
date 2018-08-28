ALTER TABLE accounts ADD last_updated TIMESTAMP ;
UPDATE accounts SET last_updated = created;