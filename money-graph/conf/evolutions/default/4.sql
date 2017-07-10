--- Initial DB schema for party members management

# --- !Ups

CREATE TABLE party_members
(
  party_id      INT REFERENCES parties (id) NOT NULL,
  member_id     INT REFERENCES accounts (id) NOT NULL,
  UNIQUE (party_id, member_id)
);

# --- !Downs

DROP TABLE IF EXISTS party_members CASCADE;