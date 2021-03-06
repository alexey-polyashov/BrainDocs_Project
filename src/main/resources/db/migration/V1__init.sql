--TABLES

--CONTACTS TYPE
CREATE TABLE IF NOT EXISTS contacts_types
(
    id bigserial,
    name varchar NOT NULL,
    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT contacts_types_pkey PRIMARY KEY (id)
);
INSERT INTO contacts_types (name)
VALUES ('Телефон'),
       ('Адрес');

--ORGANISATIONS
CREATE TABLE IF NOT EXISTS organisations
(
    id bigserial,
    name varchar,
    inn character varying(12) NOT NULL,
    kpp character varying(9),
    manager bigint,
    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT organisations_pkey PRIMARY KEY (id)

);
--CONTACTS
CREATE TABLE IF NOT EXISTS organisation_contacts
(
    id bigserial,
    organisation bigint NOT NULL,
    type bigint NOT NULL,
    present varchar NOT NULL,
    CONSTRAINT organisation_contacts_pkey PRIMARY KEY (id),
    CONSTRAINT fk_organisation_id FOREIGN KEY (organisation)
        REFERENCES organisations (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT type_id FOREIGN KEY (type)
         REFERENCES contacts_types (id)
         ON UPDATE NO ACTION
         ON DELETE CASCADE
);
INSERT INTO organisations (name, inn, kpp)
VALUES ('ООО "Ромашка"', '1234567890', '098765432'),
       ('ИП Иванов', '123456789012', '');
INSERT INTO organisation_contacts (organisation, type, present)
VALUES (1, 2,'Москва, Пушкино, дом 7, офис 11'),
       (1, 1,'8(495) 137-83-90'),
       (2, 2,'Санкт-Петербург, '),
       (2, 1,'8(926) 401-90-90');

--USERS
CREATE TABLE IF NOT EXISTS users
(
    id bigserial,
    login varchar NOT NULL,
    email varchar NOT NULL,
    fullname varchar,
    shortname varchar NOT NULL,
    organisation bigint,
    male character varying(6),
    birthday date,
    password   character varying(80) NOT NULL,
    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT organisation_id FOREIGN KEY (organisation)
        REFERENCES organisations (id)
);
INSERT INTO users (login, password, email, fullname, shortname, organisation)
VALUES ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'bob_johnson@gmail.com', 'Сильвер Стивенсон', 'Сильвер С.', 1),
       ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'john_johnson@gmail.com', 'Ханс Циммер', 'Ханс Ц.', 1),
       ('user2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'nick_nickson@gmail.com', 'Шварцнегер Арнольд', 'Шварцнегер А.', 1);
CREATE TABLE IF NOT EXISTS user_contacts
(
    id bigserial,
    userid bigint NOT NULL,
    type bigint NOT NULL,
    present varchar NOT NULL,
    CONSTRAINT user_contacts_pkey PRIMARY KEY (id),
    CONSTRAINT fk_type_id FOREIGN KEY (type)
        REFERENCES contacts_types (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_userid_id FOREIGN KEY (userid)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
INSERT INTO user_contacts (userid, type, present)
VALUES (3, 1,'222-22-22'),
       (1, 1,'8(495) 137-83-90'),
       (2, 1,'8(926) 401-90-90');
CREATE TABLE  IF NOT EXISTS roles
(
    id         bigserial,
    name       varchar(50) not null,
    marked     boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);
CREATE TABLE users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    CONSTRAINT fk_userkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_role_id FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 1);

--DOCUMENTS TYPE
CREATE TABLE IF NOT EXISTS documents_types
(
    id bigserial,
    name varchar NOT NULL,
    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT documents_types_pkey PRIMARY KEY (id)
);
INSERT INTO documents_types (name)
VALUES ('Приказы'),
       ('Договора'),
       ('Прочие');

--DOCUMENTS
CREATE TABLE IF NOT EXISTS documents
(
    id bigserial,
    type bigint NOT NULL,
    number character varying(50),
    document_date date,
    heading varchar,
    content varchar,
    author bigint,
    responsible bigint,
    organisation bigint,
    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT documents_pkey PRIMARY KEY (id),
    CONSTRAINT author_id FOREIGN KEY (author)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_type_document FOREIGN KEY (type)
        REFERENCES documents_types (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_responsible_document FOREIGN KEY (responsible)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_organisation_document FOREIGN KEY (organisation)
        REFERENCES organisations (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
INSERT INTO documents (type, number, document_date, heading, content, author, responsible, organisation)
VALUES (1,'7',  '2021-12-20', 'Приказ о премировании', 'Примировать сотрудника Ханса Цимера за рац.предложение', 1, 1, 1),
       (1,'5',  '2021-12-12', 'Приказ о приеме на работу водителя', 'Приказываю принять на должность водителя автобуса Петрова А.В. с окладом согласно штатного расписания', 1, 1, 1),
       (2,'1/7',  '2021-12-10', 'Договор на покупку автотранспорта', 'Содержание договора', 2, 1, 1),
       (3, '321', '2021-12-05', 'Рац. предложение о покупке служебного автобуса', 'Предлагаю купить служебный автобус для экономии бюджета компании', 2, 1, 1);
