CREATE TABLE academic_years (
       id UUID PRIMARY KEY,
       start_year INT NOT NULL UNIQUE,
       end_year INT NOT NULL
);

CREATE TABLE semesters (
       id UUID PRIMARY KEY,
       academic_year_id UUID NOT NULL,
       name VARCHAR(255) NOT NULL,
       start_date TIMESTAMP WITH TIME ZONE NOT NULL,
       end_date TIMESTAMP WITH TIME ZONE NOT NULL,
       practice_diary_start TIMESTAMP WITH TIME ZONE NOT NULL,
       practice_diary_end TIMESTAMP WITH TIME ZONE NOT NULL,
       company_change_start TIMESTAMP WITH TIME ZONE NOT NULL,
       company_change_end TIMESTAMP WITH TIME ZONE NOT NULL,
       CONSTRAINT fk_academic_year
           FOREIGN KEY(academic_year_id) REFERENCES academic_years(id) ON DELETE CASCADE
);

INSERT INTO academic_years (id, start_year, end_year) VALUES
    ('1aa9cc33-718a-4034-bc07-d7ef8b4ff772', 2024, 2025);

INSERT INTO semesters (id, academic_year_id, name, start_date, end_date,
                       practice_diary_start, practice_diary_end, company_change_start, company_change_end)
VALUES
    (
        '6469a666-8aae-414b-94d4-545f8f5dcb23',
        '1aa9cc33-718a-4034-bc07-d7ef8b4ff772',
        'Первый семестр',
        '2024-09-01T00:00:00+03:00',
        '2025-02-01T00:00:00+03:00',
        '2024-09-01T00:00:00+03:00',
        '2025-01-20T00:00:00+03:00',
        '2024-11-01T00:00:00+03:00',
        '2025-01-01T00:00:00+03:00'
    ),
    (
        '95b6165c-5006-4a10-9ccd-bdbe79bfe5a0',
        '1aa9cc33-718a-4034-bc07-d7ef8b4ff772',
        'Второй семестр',
        '2025-02-02T00:00:00+03:00',
        '2025-07-01T00:00:00+03:00',
        '2025-02-15T00:00:00+03:00',
        '2025-06-20T00:00:00+03:00',
        '2025-03-01T00:00:00+03:00',
        '2025-06-01T00:00:00+03:00'
    );
