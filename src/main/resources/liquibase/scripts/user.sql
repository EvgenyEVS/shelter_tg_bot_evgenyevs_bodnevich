-- liquibase formatted sql

-- changeset evs:1
CREATE TABLE testConnectionDB
(
    id      BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL
);

-- changeset evs:2
CREATE TABLE cats_shelter
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    birth_day DATE,
    gender VARCHAR(50),
    castrated_or_spayed BOOLEAN DEFAULT FALSE,
    shelter_type VARCHAR(50),
    pet_status VARCHAR(50),
    pet_description TEXT,
    pet_info TEXT,
    special_needs TEXT,
    owner_chat_id BIGINT
)