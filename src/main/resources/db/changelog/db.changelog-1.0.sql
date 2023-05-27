--liquibase formatted sql

--changeset afedyakov:1
CREATE SCHEMA IF NOT EXISTS smmassistant;
--rollback DROP SCHEMA smmassistant;



--changeset afedyakov:2
CREATE TABLE IF NOT EXISTS smmassistant.users
(
    id          INT             PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username    VARCHAR(64)     NOT NULL UNIQUE,
    firstname    VARCHAR(64),
    lastname    VARCHAR(64)
);

COMMENT ON TABLE smmassistant.users IS 'Таблица пользователей';

COMMENT ON COLUMN smmassistant.users.id             IS 'Уникальный идентификатор пользователя';
COMMENT ON COLUMN smmassistant.users.username       IS 'Email почта польщзователя';
COMMENT ON COLUMN smmassistant.users.firstname       IS 'Имя пользователя';
COMMENT ON COLUMN smmassistant.users.lastname       IS 'Фамилия пользователя';
--rollback DROP TABLE smmassistant.users;



--changeset afedyakov:3
CREATE TABLE IF NOT EXISTS smmassistant.social_network
(
    id              BIGINT          PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(16)     NOT NULL,
    account_id      BIGINT          NOT NULL,
    access_token    VARCHAR(256)    NOT NULL,
    user_id         INT             NOT NULL REFERENCES smmassistant.users(id) ON DELETE CASCADE,
    CONSTRAINT social_network_unique UNIQUE (name, account_id, user_id)
);

COMMENT ON TABLE smmassistant.social_network IS 'Таблица аккаунтов социальных сетей';

COMMENT ON COLUMN smmassistant.social_network.id           IS 'Уникальный идентификатор социальной сети';
COMMENT ON COLUMN smmassistant.social_network.name         IS 'Имя социальной сети';
COMMENT ON COLUMN smmassistant.social_network.account_id   IS 'Идентификатор страницы пользователя социальной сети';
COMMENT ON COLUMN smmassistant.social_network.access_token IS 'Токен пользователя социальной сети';
COMMENT ON COLUMN smmassistant.social_network.user_id      IS 'Идентификатор пользователя';
--rollback DROP TABLE smmassistant.social_network;



--changeset afedyakov:4
CREATE TABLE IF NOT EXISTS smmassistant.publication
(
    id              BIGINT          PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    message         VARCHAR(2048),
    attachments     VARCHAR(128),
    publish_date    TIMESTAMP       NOT NULL,
    user_id         INT             NOT NULL REFERENCES smmassistant.users(id) ON DELETE CASCADE
);

COMMENT ON TABLE smmassistant.publication IS 'Таблица публикаций';

COMMENT ON COLUMN smmassistant.publication.id           IS 'Уникальный идентификатор публикации';
COMMENT ON COLUMN smmassistant.publication.message      IS 'Текст сообщения публикации';
COMMENT ON COLUMN smmassistant.publication.attachments  IS 'Объект или несколько объектов, приложенных к записи';
COMMENT ON COLUMN smmassistant.publication.publish_date IS 'Дата и время публикации';
COMMENT ON COLUMN smmassistant.publication.user_id      IS 'Идентификатор пользователя';
--rollback DROP TABLE smmassistant.publication;



--changeset afedyakov:5
CREATE TABLE IF NOT EXISTS smmassistant.publication_info
(
    id                  BIGINT          PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    social_network_name VARCHAR(16)     NOT NULL,
    post_id             BIGINT          NOT NULL,
    link                VARCHAR(128)    NOT NULL,
    publication_id      BIGINT          NOT NULL REFERENCES smmassistant.publication(id) ON DELETE CASCADE
);

COMMENT ON TABLE smmassistant.publication IS 'Таблица информации о публикациях';

COMMENT ON COLUMN smmassistant.publication_info.id              IS 'Уникальный идентификатор информации о публикации';
COMMENT ON COLUMN smmassistant.social_network.name              IS 'Имя социальной сети';
COMMENT ON COLUMN smmassistant.publication_info.post_id         IS 'Идентификатор публикации в сети';
COMMENT ON COLUMN smmassistant.publication_info.link            IS 'Ссылка на публикацию';
COMMENT ON COLUMN smmassistant.publication_info.publication_id  IS 'Идентификатор публикации';
--rollback DROP TABLE smmassistant.publication_info;
