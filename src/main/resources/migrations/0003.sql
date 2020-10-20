--liquibase formatted sql

--changeset jwirth:3
CREATE TABLE IF NOT EXISTS friends (id SERIAL , user_id_a BIGINT NOT NULL , user_id_b BIGINT NOT NULL , PRIMARY KEY (`id`) , FOREIGN KEY (`user_id_a`) REFERENCES user(`id`) ON DELETE CASCADE , FOREIGN KEY (`user_id_b`) REFERENCES user(`id`) ON DELETE CASCADE);
--rollback drop table friends;