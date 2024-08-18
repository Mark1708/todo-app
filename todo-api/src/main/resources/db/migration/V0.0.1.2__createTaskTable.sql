create table todo_task
(
    id          uuid          not null primary key,
    title       varchar(128)  not null,
    description varchar(1000) not null,
    done        boolean       not null default false,
    created_at  timestamp     not null,
    updated_at  timestamp     not null
);