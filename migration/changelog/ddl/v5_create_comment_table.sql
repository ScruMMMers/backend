CREATE TABLE comment
(
    id         UUID PRIMARY KEY,
    author     UUID        NOT NULL,
    message    TEXT        NOT NULL,
    replyTo    UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NULL,
    isDeleted  BOOLEAN     NOT NULL
)