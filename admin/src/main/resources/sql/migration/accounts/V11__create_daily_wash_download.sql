CREATE TABLE daily_wash_download (
  creation_id   UUID,
  type          VARCHAR(10),
  username      VARCHAR(200) NOT NULL,
  download_time TIMESTAMP    NOT NULL, -- FIXME: this must be TIMESTAMPTZ

  PRIMARY KEY (creation_id, type),

  FOREIGN KEY (creation_id, type) REFERENCES daily_wash_content (creation_id, type)
);

CREATE INDEX daily_wash_download_download_time_index
  ON daily_wash_download (download_time DESC);