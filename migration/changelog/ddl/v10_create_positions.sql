CREATE TABLE positions (
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(255) NOT NULL,
      position VARCHAR(50) NOT NULL
);

INSERT INTO positions (name, position) VALUES
       ('Kotlin', 'BACKEND'),
       ('Java', 'BACKEND'),
       ('NET', 'BACKEND'),
       ('Python', 'BACKEND'),
       ('React', 'FRONTEND'),
       ('Vue', 'FRONTEND'),
       ('Android', 'MOBILE'),
       ('iOS', 'MOBILE'),
       ('ML', 'ML'),
       ('1C', 'ONE_S');


CREATE TABLE log_positions (
       log_id UUID NOT NULL,
       position_id BIGINT NOT NULL,
       PRIMARY KEY (log_id, position_id),
       CONSTRAINT fk_log_positions_log
           FOREIGN KEY (log_id) REFERENCES logs(id) ON DELETE CASCADE,
       CONSTRAINT fk_log_positions_position
           FOREIGN KEY (position_id) REFERENCES positions(id) ON DELETE CASCADE
);