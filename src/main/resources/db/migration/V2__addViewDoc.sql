--DOCUMENTS view
CREATE TABLE IF NOT EXISTS documents_view
(
    id bigserial,
--    type bigint NOT NULL,
    name text NOT NULL,
    data text,
--    created_at timestamp without time zone,
--    updated_at timestamp without time zone,
    CONSTRAINT documents_storage_pkey PRIMARY KEY (id)
--    CONSTRAINT fk_type_document_storage FOREIGN KEY (type)
--            REFERENCES documents_types (id)
--            ON UPDATE NO ACTION
--            ON DELETE NO ACTION
);
INSERT INTO documents_view (name ,data)
VALUES ('приказ','fsgsdfhsdfhsdfsufgusjsghshsfhs'),
       ('заказ','shfshshsfhshfhfs'),
       ('талон','dhgdfggdggggggggggggggggggggdgddddddddddddddddd');