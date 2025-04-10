CREATE TABLE company_position_new
(
    id          UUID PRIMARY KEY,
    company_id  UUID   NOT NULL,
    position_id BIGINT NOT NULL DEFAULT 1,
    FOREIGN KEY (company_id) REFERENCES company (id)
);

INSERT INTO company_position_new (id, company_id, position_id)
SELECT position_id,
       company_id,
       1
FROM company_position;

DROP TABLE company_position;

ALTER TABLE company_position_new RENAME TO company_position;