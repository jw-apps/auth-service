--liquibase formatted sql

--changeset jwirth:4
CREATE TABLE IF NOT EXISTS friendRequests (id SERIAL , user_id_a BIGINT UNSIGNED NOT NULL , user_id_b BIGINT UNSIGNED NOT NULL , PRIMARY KEY (`id`) , CONSTRAINT FRR_FK FOREIGN KEY (user_id_a) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE , CONSTRAINT FRR_FK_2 FOREIGN KEY (user_id_b) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE);
--rollback drop table friendRequests;