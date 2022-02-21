--TABLES

--FILES FOR DOCUMENT TYPES
CREATE TABLE IF NOT EXISTS documenttypes_files
(
    ownerid bigserial not null,
    fileid bigserial,
    CONSTRAINT fk_documenttypes_files_ownerid FOREIGN KEY (ownerid)
        REFERENCES documents_types (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_documenttypes_files_fileid FOREIGN KEY (fileid)
        REFERENCES files (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);



