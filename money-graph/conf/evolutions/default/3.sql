--- Initial DB schema for money management

# --- !Ups

CREATE TABLE money
(
  id              SERIAL   PRIMARY KEY               NOT NULL,
  debitor_id      INT      REFERENCES accounts (id)  NOT NULL,
  creditor_id     INT      REFERENCES accounts (id)  NOT NULL,
  value           FLOAT                              NOT NULL,
  party_id        INT                                NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS money CASCADE;