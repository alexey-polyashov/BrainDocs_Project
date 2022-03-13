--TABLES

--TASK TYPES
CREATE TABLE IF NOT EXISTS task_types
(
    id bigserial,
    typename varchar,
    marked boolean NOT NULL default FALSE,
    CONSTRAINT task_types_pkey PRIMARY KEY (id)
);
INSERT INTO task_types (typename)
VALUES ('Исполнение'),
       ('Согласование'),
       ('Подписание'),
       ('Ознакомление');

--RESULTS
CREATE TABLE IF NOT EXISTS task_results
(
    id bigserial,
    task_type_id bigint NOT NULL,
    resultname varchar,
    marked boolean NOT NULL default FALSE,
    CONSTRAINT task_results_pkey PRIMARY KEY (id)
);
INSERT INTO task_results (task_type_id, resultname)
VALUES (1,'Исполнено'),
       (1,'Не исполнено'),
       (2,'Согласовано'),
       (2,'Согласовано с замечаниями'),
       (2,'Не согласовано'),
       (3,'Подписан'),
       (3,'Не подписан'),
       (4,'Ознакомлен');

--TASKS
CREATE TABLE IF NOT EXISTS tasks
(
    id bigserial,
    type_id bigint NOT NULL,
    heading varchar NOT NULL,
    content varchar,
    status int, --1 активна, 2- выполнена, 3- отменена
    author_id bigint NOT NULL,
    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT type_id_task_fkey FOREIGN KEY (type_id)
        REFERENCES task_types (id)
);

--TASK_EXECUTORS
CREATE TABLE IF NOT EXISTS task_executors
(
    id bigserial,
    task_id bigint NOT NULL,
    executor_id bigint NOT NULL,
    created_at timestamp without time zone,
    planed_date timestamp without time zone,
    date_of_comletion timestamp without time zone,
    comment varchar,
    result_id bigint NOT NULL,
    status int, --1 ожидает выполнения, 2- в работе, 3- выполнена, 4- отменена, 5- уточнение
    CONSTRAINT task_executors_pk PRIMARY KEY (id),
    CONSTRAINT executors_of_task_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT exucutor_of_task_user_fk FOREIGN KEY (executor_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
     CONSTRAINT exucutor_result_fk FOREIGN KEY (result_id)
         REFERENCES task_results (id)
         ON UPDATE NO ACTION
         ON DELETE CASCADE
);

--TASK_SUBJECTS
CREATE TABLE IF NOT EXISTS task_subjects
(
    task_id bigserial not null,
    subject_id bigserial,
    CONSTRAINT task_subjects_task_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT task_subjects_subject_fk FOREIGN KEY (subject_id)
        REFERENCES documents (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

--FILES FOR TASKS
CREATE TABLE IF NOT EXISTS tasks_files
(
    ownerid bigserial not null,
    fileid bigserial,
    CONSTRAINT fk_tasks_files_ownerid FOREIGN KEY (ownerid)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_tasks_files_fileid FOREIGN KEY (fileid)
        REFERENCES files (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

--COMMENT
CREATE TABLE IF NOT EXISTS task_comments
(
    id bigserial,
    task_id bigint NOT NULL,
    author_id bigint NOT NULL,
    comment varchar,
    created_at timestamp without time zone,
    CONSTRAINT task_comments_pk PRIMARY KEY (id),
    CONSTRAINT task_comments_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
