CREATE TABLE log_files (
        log_id UUID NOT NULL,
        file_id UUID NOT NULL,
        PRIMARY KEY (log_id, file_id),
        CONSTRAINT fk_log_files_log FOREIGN KEY (log_id) REFERENCES logs (id) ON DELETE CASCADE,
        CONSTRAINT fk_log_files_file FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);
