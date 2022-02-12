--TABLES

--STORAGE TYPE
CREATE TABLE IF NOT EXISTS filestorage_type
(
    id int,
    name text  NOT NULL
);
INSERT INTO filestorage_type (id, name)
VALUES (1, 'В базе'),
       (2, 'На диске');

--OPTIONS
CREATE TABLE IF NOT EXISTS options
(
    id int,
    file_storage_type int  NOT NULL
);


--FILES
CREATE TABLE IF NOT EXISTS files
(
    id bigserial,
    storagetype int NOT NULL,
    name character varying(250) not null,
    describtion character varying(250) not null,
    filesize bigint,
    filetype character varying(15),
    author bigint,
    parsedtext text,
    originalfilename text,
    contenttype text,
    filedata oid not null,

    marked boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT files_pkey PRIMARY KEY (id),
    CONSTRAINT fk_files_author_id FOREIGN KEY (author)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_files_type FOREIGN KEY (filetype)
        REFERENCES filestorage_type (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

--FILES FOR DOCUMENTS
CREATE TABLE IF NOT EXISTS documents_files
(
    ownerid bigserial not null,
    fileid bigserial,
    CONSTRAINT fk_documents_files_ownerid FOREIGN KEY (ownerid)
        REFERENCES documents (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_documents_files_fileid FOREIGN KEY (fileid)
        REFERENCES files (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);



