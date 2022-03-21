--TABLES

INSERT INTO tasks (type_id, heading, content, status, author_id, marked, created_at, updated_at)
VALUES (2, 'Согласовать документ', 'Проверить документ и согласовать', 1, 1, false, '2022-01-02 08:10', '2022-01-02 08:10'),
       (3, 'Подписать документ', 'Проверить документ и подписать', 1, 1, false, '2022-01-02 08:10', '2022-01-02 08:10')
;
INSERT INTO task_subjects (task_id, subject_id)
VALUES (1, 2),
       (2, 3)
;

INSERT INTO task_executors (task_id, executor_id, created_at, planed_date, status)
VALUES (1, 2, '2022-01-02 08:10', '2022-01-10 08:10', 1),
       (2, 1, '2022-01-02 08:10', '2022-01-10 08:10', 1)
;

INSERT INTO task_comments (task_id, author_id, comment, created_at)
VALUES (1, 2, 'какой-то комментарий', '2022-01-02 08:10'),
       (2, 1, 'съешь ещё этих мягких французских булок, да выпей чаю', '2022-01-12 08:10'),
       (1, 1, 'съешь ещё этих мягких французских булок, да выпей чаю', '2022-02-02 08:10')
;

