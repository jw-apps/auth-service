--liquibase formatted sql

--changeset jwirth:2
CREATE TABLE IF NOT EXISTS invitations (id SERIAL , invitation VARCHAR(50) NOT NULL , user_id BIGINT UNSIGNED NOT NULL , PRIMARY KEY (`id`) , CONSTRAINT INV_FK FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE)
--rollback drop table invitations;