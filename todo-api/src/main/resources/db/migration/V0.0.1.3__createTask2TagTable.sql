create table todo_task_tag
(
    task_id  uuid references todo_task (id),
    tag_id uuid references  todo_tag (id),
    constraint todo_task_tag_pk primary key (task_id, tag_id)
);


