create table employees
(
    user_id    uuid                     not null,
    created_at timestamp with time zone not null,
    primary key (user_id)
);

create table employees_companies
(
    user_id uuid not null,
    company_id uuid not null,
    foreign key (company_id) references company (id),
    primary key (user_id, company_id)
);
