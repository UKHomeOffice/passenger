ALTER TABLE visa ADD status VARCHAR(200);
ALTER TABLE visa ADD reason VARCHAR(2000);
UPDATE visa SET status = 'ISSUED';
