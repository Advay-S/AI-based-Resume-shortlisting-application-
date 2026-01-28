CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS candidate_profiles (
    id SERIAL PRIMARY KEY,
    full_text TEXT,
    embedding vector(384)
);