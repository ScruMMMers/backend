ALTER TABLE students
    ADD COLUMN position_id bigint references positions(id) DEFAULT NULL;
