ALTER TABLE logs
    ADD CONSTRAINT fk_logs_user FOREIGN KEY (user_id) REFERENCES students(user_id) ON DELETE CASCADE;

ALTER TABLE log_reactions DROP CONSTRAINT IF EXISTS fk_log_reactions_log;
ALTER TABLE log_reactions
    ADD CONSTRAINT fk_log_reactions_log FOREIGN KEY (log_id)
        REFERENCES logs (id) ON DELETE CASCADE;

ALTER TABLE log_tags DROP CONSTRAINT IF EXISTS fk_log_tags_log;
ALTER TABLE log_tags
    ADD CONSTRAINT fk_log_tags_log FOREIGN KEY (log_id)
        REFERENCES logs (id) ON DELETE CASCADE;

ALTER TABLE log_files DROP CONSTRAINT IF EXISTS fk_log_files_log;
ALTER TABLE log_files
    ADD CONSTRAINT fk_log_files_log FOREIGN KEY (log_id)
        REFERENCES logs (id) ON DELETE CASCADE;

ALTER TABLE log_positions DROP CONSTRAINT IF EXISTS fk_log_positions_log;
ALTER TABLE log_positions
    ADD CONSTRAINT fk_log_positions_log FOREIGN KEY (log_id)
        REFERENCES logs (id) ON DELETE CASCADE;

ALTER TABLE employees_companies
    ADD CONSTRAINT fk_employees_companies_user
        FOREIGN KEY (user_id)
            REFERENCES employees (user_id) ON DELETE CASCADE;
