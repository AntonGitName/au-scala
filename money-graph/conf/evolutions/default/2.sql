--- Initial DB schema for parties management

# --- !Ups

CREATE TABLE parties
(
  id              SERIAL        PRIMARY KEY NOT NULL,
  description     TEXT
);

# --- !Downs

DROP TABLE IF EXISTS parties CASCADE;