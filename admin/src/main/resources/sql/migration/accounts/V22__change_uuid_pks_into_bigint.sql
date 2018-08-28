-- new field with new name, unique
ALTER TABLE daily_wash_creation ADD COLUMN id2 BIGSERIAL;
ALTER TABLE daily_wash_creation ADD CONSTRAINT id2_unq UNIQUE(id2);

-- new field with new name
ALTER TABLE daily_wash_download ADD COLUMN creation_id2 BIGINT;

-- join tables on new fields as per existing fk constraint
update daily_wash_download SET creation_id2 = ( SELECT id2 FROM daily_wash_creation WHERE daily_wash_creation.id = daily_wash_download.creation_id);

-- add fk constraint on new fields
ALTER TABLE daily_wash_download ADD CONSTRAINT daily_wash_download_creation_id2_fkey FOREIGN KEY (creation_id2) REFERENCES daily_wash_creation (id2);

-- drop old fk constraint
ALTER TABLE daily_wash_download DROP CONSTRAINT daily_wash_download_creation_id_fkey;

-- replace daily_wash_creation pk
ALTER TABLE daily_wash_creation DROP CONSTRAINT daily_wash_creation_pkey;
ALTER TABLE daily_wash_creation ADD CONSTRAINT daily_wash_creation_pkey PRIMARY KEY (id2);

-- temporarily drop vie wto allow changing referenced fields
DROP VIEW daily_wash_view;

-- creation uuid is kept, unique
ALTER TABLE daily_wash_creation RENAME COLUMN id TO uuid;
ALTER TABLE daily_wash_creation ADD CONSTRAINT uuid_unique_index UNIQUE(uuid);

-- pk is still named id
ALTER TABLE daily_wash_creation RENAME COLUMN id2 TO id;

-- drop old constraints
ALTER TABLE daily_wash_download DROP CONSTRAINT daily_wash_download_creation_id2_fkey;
ALTER TABLE daily_wash_download DROP CONSTRAINT daily_wash_download_pkey;

-- drop old fields
ALTER TABLE daily_wash_download DROP COLUMN creation_id;

-- rename new fields to old names
ALTER TABLE daily_wash_download RENAME COLUMN creation_id2 TO creation_id;

-- recreate old constraints
ALTER TABLE daily_wash_download ADD CONSTRAINT daily_wash_download_pkey PRIMARY KEY (creation_id, type);

-- remove temporary unique constraint
ALTER TABLE daily_wash_creation DROP CONSTRAINT id2_unq;

-- fk with new fields
ALTER TABLE daily_wash_download ADD CONSTRAINT daily_wash_download_creation_id_fkey FOREIGN KEY (creation_id) REFERENCES daily_wash_creation (id);

-- recreate view as it was
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

