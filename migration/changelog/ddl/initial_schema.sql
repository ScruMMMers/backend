CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

create table category
(
    id   uuid        not null,
    name VARCHAR(30) not null,
    primary key (id)
);
