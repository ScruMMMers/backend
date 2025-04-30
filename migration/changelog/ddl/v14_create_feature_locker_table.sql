CREATE TABLE feature_locker
(
    feature_name VARCHAR(50) PRIMARY KEY,
    is_locked    BOOLEAN NOT NULL
);

INSERT INTO feature_locker (feature_name, is_locked)
VALUES ('SUBMITTING_PRACTICE_DIARY', false);

INSERT INTO feature_locker (feature_name, is_locked)
VALUES ('CHANGING_INTERNSHIP_LOCATION', false);
