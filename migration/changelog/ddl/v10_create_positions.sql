CREATE TABLE positions (
      id SERIAL PRIMARY KEY,
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