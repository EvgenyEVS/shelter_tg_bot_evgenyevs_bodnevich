-- liquibase formatted sql

-- changeset evs:1

CREATE TABLE IF NOT EXISTS pets
(
    id BIGSERIAL PRIMARY KEY,
    pet_type VARCHAR(50),
    pet_name VARCHAR(255),
    birth_day DATE,
    gender VARCHAR(50),
    castrated_or_spayed BOOLEAN DEFAULT FALSE,
    pet_status VARCHAR(50),
    pet_description TEXT,
    health_info TEXT,
    special_needs TEXT,
    shelter_id BIGINT,
    owner_id BIGINT
);

CREATE TABLE IF NOT EXISTS shelter
(
    id BIGSERIAL PRIMARY KEY,
    pet_type VARCHAR(50),
    address VARCHAR(255),
    shelter_info TEXT,
    shelter_schedule TEXT,
    route_schema_url VARCHAR(255),
    contacts VARCHAR(255),
    safety_precautions_at_shelter TEXT
);

CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT,
    telegram_user_name VARCHAR(255),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INTEGER,
    phone_number VARCHAR(50),
    selected_shelter_type VARCHAR(50),
    user_status VARCHAR(50),
    volunteer BOOLEAN DEFAULT FALSE,
    dialog_state VARCHAR(50),
    adoptional_start_date TIMESTAMP
    );

ALTER TABLE pets ADD CONSTRAINT fk_pets_shelter
    FOREIGN KEY (shelter_id) REFERENCES shelter(id) ON DELETE SET NULL;

ALTER TABLE pets ADD CONSTRAINT fk_pets_owner
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL;

CREATE INDEX idx_pets_shelter_id ON pets(shelter_id);
CREATE INDEX idx_pets_owner_id ON pets(owner_id);
CREATE INDEX idx_users_chat_id ON users(chat_id);

-- changeset evs:2

CREATE TABLE IF NOT EXISTS reports
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    report_date DATE NOT NULL,
    photo_url VARCHAR(255),
    diet TEXT,
    health_and_adaptation TEXT,
    behavior_changes TEXT,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed BOOLEAN DEFAULT FALSE,
    volunteer_feedback TEXT,
    CONSTRAINT fk_reports_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_reports_user_id ON reports(user_id);
CREATE INDEX idx_reports_report_date ON reports(report_date);

CREATE TABLE IF NOT EXISTS adoptions
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    start_date DATE,
    missed_days INTEGER DEFAULT 0,
    probation_status VARCHAR(50) DEFAULT 'IN_PROGRESS',
    probation_end_date DATE,

    CONSTRAINT fk_adoptions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_adoptions_pet FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    CONSTRAINT uk_adoptions_user_pet UNIQUE (user_id, pet_id)
);

CREATE INDEX idx_adoptions_user_id ON adoptions(user_id);
CREATE INDEX idx_adoptions_pet_id ON adoptions(pet_id);
CREATE INDEX idx_adoptions_status ON adoptions(probation_status);

-- changeset evs:3
CREATE TABLE IF NOT EXISTS adoption_info
(
    id BIGSERIAL PRIMARY KEY,
    pet_type VARCHAR(50),
    advice_before TEXT,
    document_set TEXT,
    advice_transport TEXT,
    advice_home_for_child TEXT,
    advice_home_for_adult TEXT,
    advice_home_for_disabilities TEXT,
    refusal_set TEXT
    );

CREATE TABLE IF NOT EXISTS adoption_info_cat
(
    id BIGSERIAL PRIMARY KEY,
    cat_primary_communication TEXT,
    cat_trainers TEXT
);

CREATE TABLE IF NOT EXISTS adoption_info_dog
(
    id BIGSERIAL PRIMARY KEY,
    dog_primary_communication TEXT,
    dog_trainers TEXT
);

