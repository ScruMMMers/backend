CREATE TABLE feature_locker
(
    id           uuid    not null PRIMARY KEY,
    feature_name VARCHAR(255) PRIMARY KEY,
    is_locked    BOOLEAN NOT NULL
);

INSERT INTO feature_locker (id, feature_name, is_locked)
VALUES ('9cffdf5b-e301-4662-8ea0-a192b38b4aa4', 'SUBMITTING_PRACTICE_DIARY', false);

INSERT INTO feature_locker (id, feature_name, is_locked)
VALUES ('4482ff59-e4eb-4374-9cec-c60811ecb1d5', 'CHANGING_INTERNSHIP_LOCATION', false);
