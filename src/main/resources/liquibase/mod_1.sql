--liquibase formatted sql

--changeset INIT:1

CREATE TABLE company (
    companyId serial PRIMARY KEY,
    companyName VARCHAR ( 50 ) UNIQUE NOT NULL,
    companyKafkaCode VARCHAR ( 10 ) UNIQUE NOT NULL,
    status int NOT NULL DEFAULT 1
);

COMMENT ON COLUMN company.status IS '0 - inactive, 1 active, 2 deleted';

INSERT INTO company (companyName, companyKafkaCode)
VALUES ('Company X', 'X'),
       ('Company Y', 'Y'),
       ('Company Z', 'Z');