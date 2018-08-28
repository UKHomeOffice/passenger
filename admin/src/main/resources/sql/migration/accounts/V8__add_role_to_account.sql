ALTER TABLE accounts ADD role VARCHAR(100) ;
UPDATE accounts SET role = 'ADMIN';