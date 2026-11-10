-- Dumping structure for table demo.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_by` BIGINT(20) NULL DEFAULT NULL,
	`created_date` DATETIME(6) NOT NULL,
	`updated_by` BIGINT(20) NULL DEFAULT NULL,
	`updated_date` DATETIME(6) NOT NULL,
	`account_non_expired` TINYINT(1) NULL DEFAULT '1',
	`account_non_locked` TINYINT(1) NULL DEFAULT '1',
	`credentials_non_expired` TINYINT(1) NULL DEFAULT '1',
	`email` VARCHAR(255) NULL DEFAULT NULL,
	`enabled` BIT(1) NOT NULL,
	`first_name` VARCHAR(255) NULL DEFAULT NULL,
	`last_name` VARCHAR(255) NULL DEFAULT NULL,
	`password` VARCHAR(255) NULL DEFAULT NULL,
	`sex` VARCHAR(255) NULL DEFAULT NULL,
	`username` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `UKr43af9ap4edm43mmtq01oddj6` (`username`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;



--
-- Table structure for table `roles`
--
DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_by` BIGINT(20) NULL DEFAULT NULL,
	`created_date` DATETIME(6) NOT NULL,
	`updated_by` BIGINT(20) NULL DEFAULT NULL,
	`updated_date` DATETIME(6) NOT NULL,
	`role` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `user_authorities`;

CREATE TABLE `user_authorities` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_by` BIGINT(20) NULL DEFAULT NULL,
	`created_date` DATETIME(6) NOT NULL,
	`updated_by` BIGINT(20) NULL DEFAULT NULL,
	`updated_date` DATETIME(6) NOT NULL,
	`role_id` BIGINT(20) NULL DEFAULT NULL,
	`username_id` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK9218rxk6srmo5d10dkbwpiyqm` (`role_id`),
	INDEX `FKse0bl9w2setnqjfq2xk5tdmfs` (`username_id`),
	CONSTRAINT `FK9218rxk6srmo5d10dkbwpiyqm` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
	CONSTRAINT `FKse0bl9w2setnqjfq2xk5tdmfs` FOREIGN KEY (`username_id`) REFERENCES `users` (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;


CREATE TABLE `files` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_by` BIGINT(20) NULL DEFAULT NULL,
	`created_date` DATETIME(6) NOT NULL,
	`updated_by` BIGINT(20) NULL DEFAULT NULL,
	`updated_date` DATETIME(6) NOT NULL,
	`content_type` VARCHAR(255) NULL DEFAULT NULL,
	`file_data` LONGBLOB NULL,
	`file_id` VARCHAR(255) NOT NULL,
	`file_name` VARCHAR(255) NOT NULL,
	`file_size` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=12
;


CREATE TABLE `text_data` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_by` BIGINT(20) NULL DEFAULT NULL,
	`created_date` DATETIME(6) NOT NULL,
	`updated_by` BIGINT(20) NULL DEFAULT NULL,
	`updated_date` DATETIME(6) NOT NULL,
	`text_data` LONGTEXT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;


CREATE TABLE `rag_data` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_by` BIGINT(20) NULL DEFAULT NULL,
	`created_date` DATETIME(6) NOT NULL,
	`updated_by` BIGINT(20) NULL DEFAULT NULL,
	`updated_date` DATETIME(6) NOT NULL,
	`file_entity_id` BIGINT(20) NULL DEFAULT NULL,
	`text_entity_id` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `UKdt32vr4g8pcjcgfokcqyynsy8` (`file_entity_id`),
	UNIQUE INDEX `UKn4v4gga01tpor7vk88bnrrnlp` (`text_entity_id`),
	CONSTRAINT `FKbiwgmxuylw3mgixiikjwtc96g` FOREIGN KEY (`text_entity_id`) REFERENCES `text_data` (`id`),
	CONSTRAINT `FKcjqm4236so22280mkyfuq4bcf` FOREIGN KEY (`file_entity_id`) REFERENCES `files` (`id`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;


INSERT INTO `users` VALUES (1,NULL,NOW(),NULL,NOW(),1,1,1,'testt01021990@gmail.com',1,NULL,NULL,'$2a$10$hbxecwitQQ.dDT4JOFzQAulNySFwEpaFLw38jda6Td.Y/cOiRzDFu',NULL,"testt01021990@gmail.com");

INSERT INTO `roles` VALUES (1,1,NOW(),NULL,NOW(),'ROLE_USER');
INSERT INTO `roles` VALUES (2,1,NOW(),NULL,NOW(),'ROLE_ADMIN');

INSERT INTO `user_authorities` VALUES (1,NULL,NOW(),NULL,NOW(),1,1);


