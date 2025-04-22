create table invite_link
(
    link_id    uuid                     not null,
    config     jsonb                    not null,
    created_at timestamp with time zone not null,
    primary key (link_id)
);