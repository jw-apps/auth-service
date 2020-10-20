--liquibase formatted sql

--changeset jwirth:1
CREATE TABLE IF NOT EXISTS users (id SERIAL , username VARCHAR(50) NOT NULL , password VARCHAR(50) NOT NULL , salt VARCHAR(50) NOT NULL , email VARCHAR(50) , PRIMARY KEY (`id`));
--rollback drop table users;