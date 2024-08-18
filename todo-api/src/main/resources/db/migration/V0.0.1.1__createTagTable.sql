create table todo_tag
(
    id   uuid         not null primary key,
    name varchar(128) not null,
    unique (name)
);