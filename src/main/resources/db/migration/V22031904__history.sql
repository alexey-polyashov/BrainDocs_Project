--HISTORY
CREATE TABLE IF NOT EXISTS operations_history
(
    id          bigserial,
    change_type varchar,
    author_id   bigint NOT NULL,
    created_at  timestamp without time zone,
    CONSTRAINT operations_history_pk PRIMARY KEY (id),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);