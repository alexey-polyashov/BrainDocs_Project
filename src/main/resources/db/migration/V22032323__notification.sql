--TABLES

--OPTIONS

DROP TABLE options;

CREATE TABLE IF NOT EXISTS options
(
    id bigserial not null,
    file_storage_type int  NOT NULL,
    mail_smtp_host varchar,
    mail_smtp_port varchar,
    mail_login varchar,
    mail_password varchar,
    mail_service_email varchar,
    mail_ssl_used bool,
    mail_need_authentication bool,
    date_format varchar,
    datetime_format varchar
);

INSERT INTO options (file_storage_type,
                     mail_smtp_host,
                     mail_smtp_port,
                     mail_login,
                     mail_password,
                     mail_service_email,
                     mail_ssl_used,
                     mail_need_authentication,
                     date_format,
                     datetime_format)
VALUES (
                    1,
                    'smtp.yandex.ru',
                    '465',
                    'filecloud.service',
                    'F1i2l3e4C5l6o7d8e9',
                    'filecloud.service@yandex.ru',
                    true,
                    true,
                    'yyyy-MM-dd',
                    'yyyy-MM-dd HH:mm:ss')
;
