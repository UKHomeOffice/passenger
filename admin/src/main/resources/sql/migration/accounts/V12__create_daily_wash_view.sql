CREATE VIEW daily_wash_view AS
  SELECT c.id,
    c.document_check_filename AS filename,
    c.creation_time,
    c.creator_username,
    c.rows,
    'DOC' AS type,
    d.username,
    d.download_time
  FROM daily_wash_creation c LEFT JOIN daily_wash_download d ON d.creation_id = c.id AND d.type = 'DOC'
  UNION
  SELECT c.id,
    c.name_check_filename AS filename,
    c.creation_time,
    c.creator_username,
    c.rows,
    'NAME' AS type,
    d.username,
    d.download_time
  FROM daily_wash_creation c LEFT JOIN daily_wash_download d ON d.creation_id = c.id AND d.type = 'NAME'
  ORDER BY creation_time DESC;
