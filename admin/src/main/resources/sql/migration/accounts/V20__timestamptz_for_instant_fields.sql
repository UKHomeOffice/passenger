-- the columns altered below are part of this view: the view prevents alterations of the tables involved
DROP VIEW daily_wash_view;

ALTER TABLE daily_wash_creation
    ALTER COLUMN creation_time SET DATA TYPE timestamptz USING creation_time at time zone '-1:00';

ALTER TABLE daily_wash_download
  ALTER COLUMN download_time SET DATA TYPE timestamptz USING download_time at time zone '-1:00';

CREATE VIEW daily_wash_view AS
  SELECT c.id,
    c.document_check_filename AS filename,
    c.creation_time,
    c.creator_username,
    c.full_name AS creator_full_name,
    c.rows,
    'DOC' AS type,
    d.username,
    d.full_name AS download_full_name,
    d.download_time
  FROM daily_wash_creation c LEFT JOIN daily_wash_download d ON d.creation_id = c.id AND d.type = 'DOC'
  UNION
  SELECT c.id,
    c.name_check_filename AS filename,
    c.creation_time,
    c.creator_username,
    c.full_name AS creator_full_name,
    c.rows,
    'NAME' AS type,
    d.username,
    d.full_name AS download_full_name,
    d.download_time
  FROM daily_wash_creation c LEFT JOIN daily_wash_download d ON d.creation_id = c.id AND d.type = 'NAME'
  ORDER BY creation_time DESC, type;