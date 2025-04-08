CREATE TABLE meeting
(
    id           UUID PRIMARY KEY,
    date         TIMESTAMPTZ NOT NULL,
    place        VARCHAR(255),
    meeting_type VARCHAR(50) NOT NULL,
    company_id   UUID        NOT NULL
);

CREATE TABLE building
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE audience
(
    id          UUID PRIMARY KEY,
    building_id UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
);