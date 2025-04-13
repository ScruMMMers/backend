create table invite_link(
    link_id uuid not null,
    config jsonb not null,
    primary key (link_id)
);