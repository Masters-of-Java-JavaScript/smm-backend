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

COMMENT ON COLUMN smmassistant.users.id             IS 'Уникальный идентификатор пользователя';
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
    publish_date    TIMESTAMP                   NOT NULL,
    message         VARCHAR(2048),
    attachments     VARCHAR(128),
    link            VARCHAR(512)                NOT NULL,
    response        VARCHAR(1024)
);

COMMENT ON TABLE smmassistant.publication IS 'Таблица публикаций';

COMMENT ON COLUMN smmassistant.publication.id           IS 'Уникальный идентификатор публикации';
COMMENT ON COLUMN smmassistant.publication.user_id      IS 'Идентификатор пользователя';
COMMENT ON COLUMN smmassistant.publication.publish_date IS 'Дата и время публикации';
COMMENT ON COLUMN smmassistant.publication.message      IS 'Текст сообщения публикации';
COMMENT ON COLUMN smmassistant.publication.attachments  IS 'Объект или несколько объектов, приложенных к записи';
COMMENT ON COLUMN smmassistant.publication.link         IS 'Ссылка на публикацию';
COMMENT ON COLUMN smmassistant.publication.response     IS 'Ответ от внешнего серсива';
--rollback DROP TABLE smmassistant.publication;
