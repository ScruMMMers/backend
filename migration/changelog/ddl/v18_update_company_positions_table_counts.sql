ALTER TABLE company_position
    ADD COLUMN interviews_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN employed_count INTEGER NOT NULL DEFAULT 0;