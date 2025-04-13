create table students(
    user_id uuid not null,
    student_course integer not null,
    student_group varchar(20) not null,
    is_on_academic_leave boolean not null,
    company_id uuid,
    foreign key (company_id) references company (id),
    primary key (user_id)
);