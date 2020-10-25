--liquibase formatted sql

--changeset jwirth:3
CREATE TABLE IF NOT EXISTS friends (id SERIAL , user_id_a BIGINT UNSIGNED NOT NULL , user_id_b BIGINT UNSIGNED NOT NULL , PRIMARY KEY (`id`) , CONSTRAINT FR_FK FOREIGN KEY (user_id_a) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE , CONSTRAINT FR_FK_2 FOREIGN KEY (user_id_b) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE)
--rollback drop table friends;