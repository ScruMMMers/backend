CREATE TABLE files (
       id UUID PRIMARY KEY,
       file_key VARCHAR(255) NOT NULL UNIQUE,
       file_name VARCHAR(255) NOT NULL,
       content_type VARCHAR(100) NOT NULL,
       file_size BIGINT NOT NULL,
       created_at TIMESTAMPTZ NOT NULL,
       updated_at TIMESTAMPTZ
);