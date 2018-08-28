ALTER TABLE accounts ADD failed_attempts INTEGER ;
UPDATE accounts SET failed_attempts = 0;
ALTER TABLE accounts ALTER  failed_attempts SET DEFAULT 0;