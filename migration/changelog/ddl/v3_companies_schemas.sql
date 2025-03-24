CREATE TABLE company
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    agent         UUID         NOT NULL,
    since_year    VARCHAR(4)   NOT NULL,
    description   TEXT         NOT NULL,
    primary_color VARCHAR(6)  NOT NULL
);

CREATE TABLE company_position
(
    id               UUID PRIMARY KEY,
    company_id       UUID         NOT NULL,
    name             VARCHAR(255) NOT NULL,
    employed_count   INT          NOT NULL,
    interviews_count INT          NOT NULL,
    CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company (id) ON DELETE CASCADE
);