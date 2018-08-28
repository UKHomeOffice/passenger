-- https://stackoverflow.com/questions/2829158/truncating-all-tables-in-a-postgres-database
DO language plpgsql
$func$
BEGIN
--   IF (SELECT COUNT(*)
--       FROM pg_class
--       WHERE relkind = 'r'
--             AND relnamespace = 'public' :: regnamespace
--             AND oid :: regclass :: text != 'schema_version'
--             AND oid :: regclass :: text != 'flyway_schema_history'
--   ) > 0 THEN
    EXECUTE (SELECT 'TRUNCATE TABLE ' || string_agg(oid :: regclass :: text, ', ') || ' CASCADE'
             FROM pg_class
             WHERE relkind = 'r'
                   AND relnamespace = 'public' :: regnamespace
                   AND oid :: regclass :: text != 'schema_version'
                   AND oid :: regclass :: text != 'flyway_schema_history'
    );
--   END IF;
END
$func$
@@
-- this script must be terminated by '@@' and cannot contain '@@'
