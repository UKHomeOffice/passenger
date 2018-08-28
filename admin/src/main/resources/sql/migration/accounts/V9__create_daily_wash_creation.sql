CREATE TABLE daily_wash_creation (
  id                      UUID PRIMARY KEY,
  creation_time           TIMESTAMP    NOT NULL, -- FIXME: this must be TIMESTAMPTZ
  rows                    INTEGER      NOT NULL,
  creator_username        VARCHAR(200) NOT NULL,
  document_check_filename VARCHAR(200) NOT NULL,
  name_check_filename     VARCHAR(200) NOT NULL
);

CREATE INDEX daily_wash_creation_creation_time_index
  ON daily_wash_creation (creation_time ASC);

