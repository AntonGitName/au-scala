--- Initial DB schema for identity management

# --- !Ups

create table accounts (
  id              SERIAL        PRIMARY KEY NOT NULL,
  email           TEXT          UNIQUE      NOT NULL,
  name            TEXT                      NOT NULL,
  password        TEXT                      NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS accounts CASCADE;