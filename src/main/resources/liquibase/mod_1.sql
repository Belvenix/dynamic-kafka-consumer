--liquibase formatted sql

--changeset INIT:1

CREATE TABLE company
(
    company_id          serial PRIMARY KEY,
    company_name        VARCHAR(64) UNIQUE NOT NULL,
    company_kafka_code  VARCHAR(64)        NOT NULL,
    company_kafka_group VARCHAR(64) UNIQUE NOT NULL,
    status              int                NOT NULL DEFAULT 1
);

COMMENT
ON COLUMN company.status IS '0 - inactive, 1 active, 2 deleted';

INSERT INTO company (company_name, company_kafka_code, company_kafka_group)
VALUES ('Big company X', 'X', 'X'),
       ('Big company Y', 'Y', 'Y'),
       ('Small company Z1', 'common', 'Z1'),
       ('Small company Z2', 'common', 'Z2'),
       ('Small company Z3', 'common', 'Z3');