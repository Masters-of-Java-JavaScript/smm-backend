--liquibase formatted sql

--changeset afedyakov:1
CREATE SCHEMA IF NOT EXISTS smmassistant;
--rollback DROP SCHEMA smmassistant;

--changeset afedyakov:2
CREATE TABLE IF NOT EXISTS smmassistant.users
(
    id          BIGSERIAL       PRIMARY KEY,
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
