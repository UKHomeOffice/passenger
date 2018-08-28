DROP VIEW daily_wash_view;
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
  ORDER BY creation_time DESC;