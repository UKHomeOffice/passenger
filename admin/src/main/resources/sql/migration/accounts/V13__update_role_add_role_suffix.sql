UPDATE accounts
SET role = 'ROLE_' || role
WHERE role = 'ADMIN' OR role = 'WICU';