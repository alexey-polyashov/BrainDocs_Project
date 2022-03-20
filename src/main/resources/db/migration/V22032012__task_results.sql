--TABLES

--RESULTS
ALTER TABLE task_results
    ADD result_type integer NOT NULL; --1-positive, 2-withComments, 3-negative, 4-other

Delete From Table task_results;

INSERT INTO task_results (task_type_id, resultname, result_type)
VALUES (1,'Исполнено', 1),
       (1,'Не исполнено', 3),
       (2,'Согласовано', 1),
       (2,'Согласовано с замечаниями', 2),
       (2,'Не согласовано', 3),
       (3,'Подписан', 1),
       (3,'Не подписан', 3),
       (4,'Ознакомлен', 4);


