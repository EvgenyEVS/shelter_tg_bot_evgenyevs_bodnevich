-- liquibase formatted sql

-- changeset evs:1
CREATE TABLE testConnectionDB
(
    id          BIGSERIAL PRIMARY KEY,
    chat_id     BIGINT NOT NULL
);