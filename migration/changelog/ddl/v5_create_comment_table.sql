CREATE TABLE comment
(
    id         UUID PRIMARY KEY,
    author_id  UUID        NOT NULL,
    message    TEXT        NOT NULL,
    reply_to    UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NULL,
    is_deleted  BOOLEAN     NOT NULL
)