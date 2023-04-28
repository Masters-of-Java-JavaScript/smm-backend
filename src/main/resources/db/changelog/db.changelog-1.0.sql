--liquibase formatted sql

--changeset afedyakov:1
CREATE SCHEMA IF NOT EXISTS smmassistant;
--rollback DROP SCHEMA smmassistant;



--changeset afedyakov:2
CREATE TABLE IF NOT EXISTS smmassistant.users
(
    id          SERIAL          PRIMARY KEY,
    username    VARCHAR(64)     NOT NULL UNIQUE,
    birth_date  DATE,
    firstname    VARCHAR(64),
    lastname    VARCHAR(64),
    role        VARCHAR(32)
);

COMMENT ON TABLE smmassistant.users IS 'Таблица пользователей';

COMMENT ON COLUMN smmassistant.users.id             IS 'Идентификатор пользователя';
COMMENT ON COLUMN smmassistant.users.username       IS 'Email почта польщзователя';
COMMENT ON COLUMN smmassistant.users.birth_date     IS 'Дата рождения пользователя';
COMMENT ON COLUMN smmassistant.users.firstname       IS 'Имя пользователя';
COMMENT ON COLUMN smmassistant.users.lastname       IS 'Фамилия пользователя';
COMMENT ON COLUMN smmassistant.users.role           IS 'Роль пользователя';
--rollback DROP TABLE smmassistant.users;



--changeset afedyakov:3
CREATE TABLE IF NOT EXISTS smmassistant.publication
(
    id              BIGSERIAL                   PRIMARY KEY,
    user_id         SERIAL                      REFERENCES smmassistant.users(id) ON DELETE CASCADE,
    create_dttm     TIMESTAMP WITH TIME ZONE    NOT NULL,
    text            VARCHAR(2048),
    link            VARCHAR(512)                NOT NULL,
    owner_id        VARCHAR(64)                 NOT NULL
);

COMMENT ON TABLE smmassistant.publication IS 'Таблица публикаций';

COMMENT ON COLUMN smmassistant.publication.id           IS 'Идентификатор публикации';
COMMENT ON COLUMN smmassistant.publication.user_id      IS 'Идентификатор пользователя';
COMMENT ON COLUMN smmassistant.publication.create_dttm  IS 'Дата и время публикации с учетом тайм-зоны';
COMMENT ON COLUMN smmassistant.publication.text         IS 'Описание публикации';
COMMENT ON COLUMN smmassistant.publication.link         IS 'Ссылка на публикацию';
COMMENT ON COLUMN smmassistant.publication.owner_id     IS 'Идентификатор владельца стены, на которой размещена запись';
--rollback DROP TABLE smmassistant.publication;
