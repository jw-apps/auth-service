--liquibase formatted sql

--changeset jwirth:2
CREATE TABLE IF NOT EXISTS invitations (id SERIAL , invitation VARCHAR(50) NOT NULL , user_id BIGINT NOT NULL , PRIMARY KEY (`id`) , FOREIGN KEY (`user_id`) REFERENCES user(`id`) ON DELETE CASCADE);
--rollback drop table invitations;