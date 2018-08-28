CREATE TABLE daily_wash_content (
  creation_id  UUID REFERENCES daily_wash_creation (id),
  type         VARCHAR(10),
  content_size INTEGER  NOT NULL,
  content      TEXT NOT NULL,
  PRIMARY KEY (creation_id, type)
);


