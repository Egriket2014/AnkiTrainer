CREATE SCHEMA IF NOT EXISTS anki_trainer;

SET search_path TO anki_trainer;

CREATE TABLE IF NOT EXISTS deck_config (
    id BIGSERIAL PRIMARY KEY,
    deck_name VARCHAR(255) NOT NULL UNIQUE,
    model_name VARCHAR(255) NOT NULL,
    language VARCHAR(50) NOT NULL,
    word_field VARCHAR(255) NOT NULL,
    translation_field VARCHAR(255) NOT NULL,
    extra_field VARCHAR(255),
    review_limit INT DEFAULT 200 NOT NULL,
    new_limit INT DEFAULT 5 NOT NULL,
    last_synced_at TIMESTAMP DEFAULT NOW(),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_deck_name ON deck_config (deck_name);

CREATE TABLE IF NOT EXISTS card (
    id BIGSERIAL PRIMARY KEY,
    note_id BIGINT NOT NULL UNIQUE,
    word TEXT NOT NULL,
    translation TEXT NOT NULL,
    extra TEXT,
    deck_name VARCHAR(255) NOT NULL,
    part_of_speech VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_card_note_id ON card (note_id);
CREATE INDEX IF NOT EXISTS idx_card_part_of_speech ON card (part_of_speech);
CREATE INDEX IF NOT EXISTS idx_card_deck_name ON card (deck_name);

CREATE TABLE IF NOT EXISTS card_srs (
    id BIGSERIAL PRIMARY KEY,
    card_id BIGINT NOT NULL,
    conjugation_type VARCHAR(50) NOT NULL,
    srs_json JSONB NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_card_srs_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE,
    CONSTRAINT unique_card_srs UNIQUE (card_id, conjugation_type)
);

CREATE INDEX IF NOT EXISTS idx_card_srs_card_id ON card_srs (card_id);
CREATE INDEX IF NOT EXISTS idx_card_srs_due ON card_srs ((srs_json->>'due'));