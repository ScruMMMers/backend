CREATE TABLE mark
(
    id       UUID PRIMARY KEY         NOT NULL,
    user_id  UUID                     NOT NULL,
    mark     INTEGER                  NOT NULL,
    date     TIMESTAMP WITH TIME ZONE NOT NULL,
    semester INTEGER                  NOT NULL
);