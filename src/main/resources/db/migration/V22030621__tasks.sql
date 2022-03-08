--TABLES

--TASK TYPES
CREATE TABLE IF NOT EXISTS task_types
(
    id bigserial,
    typename text,
    CONSTRAINT task_types_pkey PRIMARY KEY (id)
);
INSERT INTO task_types (typename)
VALUES ('Исполнение'),
       ('Согласование'),
       ('Подписание'),
       ('Ознакомление');

--RESULTS
CREATE TABLE IF NOT EXISTS results
(
    id bigserial,
    task_type_id bigint NOT NULL,
    resultname text,
    CONSTRAINT result_pkey PRIMARY KEY (id)
);
INSERT INTO results (task_type_id, resultname)
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
    header text NOT NULL,
    content text,
    status int, --0 активна, 1- выполнена, 2- отменена
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
    user_id bigint NOT NULL,
    planed_date timestamp without time zone,
    date_of_comletion timestamp without time zone,
    comment text,
    result_id bigint NOT NULL,
    status int, --0 активна, 1- в работе, 2- выполнил
    marked boolean,
    CONSTRAINT task_executors_pk PRIMARY KEY (id),
    CONSTRAINT executors_of_task_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT exucutor_of_task_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
     CONSTRAINT exucutor_result_fk FOREIGN KEY (result_id)
         REFERENCES results (id)
         ON UPDATE NO ACTION
         ON DELETE CASCADE
);

--TASK_SUBJECTS
CREATE TABLE IF NOT EXISTS task_subjects
(
    id bigserial,
    task_id bigint NOT NULL,
    subject_id bigint NOT NULL,
    CONSTRAINT task_subjects_pk PRIMARY KEY (id),
    CONSTRAINT subject_task_fk FOREIGN KEY (task_id)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

--FILES FOR TASKS
CREATE TABLE IF NOT EXISTS tasks_files
(
    owner_id bigserial not null,
    file_id bigserial,
    CONSTRAINT fk_tasks_files_ownerid FOREIGN KEY (ownerid)
        REFERENCES tasks (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_tasks_files_fileid FOREIGN KEY (fileid)
        REFERENCES files (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
