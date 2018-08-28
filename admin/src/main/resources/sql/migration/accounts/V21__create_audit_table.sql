CREATE TABLE audit
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  user_name   VARCHAR(254) NOT NULL,
  team        VARCHAR(100) ,
  uri         VARCHAR(1000),
  content     VARCHAR(2000),
  result      VARCHAR(50),
  date_time   TIMESTAMP DEFAULT NOW()
);