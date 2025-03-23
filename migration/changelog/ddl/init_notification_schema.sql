create table notification
(
    id          uuid                     not null,
    title       varchar(255)             not null,
    message     text                     not null,
    created_at  timestamp with time zone not null,
    is_read     boolean                  not null,
    receiver_id uuid                     not null,
    type        integer                  not null,
    channels    integer[]                not null,
    attachment  jsonb,
    primary key (id)
);
create index notification_receiver_id_index
    on notification (receiver_id);
create index notification_type_index
    on notification (type);
