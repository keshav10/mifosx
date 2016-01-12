CREATE TABLE `ct_task_planner` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`type` INT(11) NOT NULL,
	`status` INT(11) NOT NULL,
	`date` DATE NOT NULL,
	`office_id` BIGINT(20) NOT NULL,
	`entity_type` VARCHAR(50) NULL DEFAULT NULL,
	`entity_id` BIGINT(20) NULL DEFAULT NULL,
	`createdby_id` BIGINT(20) NOT NULL,
	`created_date` DATETIME NOT NULL,
	`modifiedby_id` BIGINT(20) NULL DEFAULT NULL,
	`modified_date` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_ct_task_planner_m_office` (`office_id`),
	INDEX `FK_ct_task_planner_m_code` (`type`),
	CONSTRAINT `FK_ct_task_planner_m_code` FOREIGN KEY (`type`) REFERENCES `m_code_value` (`id`),
	CONSTRAINT `FK_ct_task_planner_m_office` FOREIGN KEY (`office_id`) REFERENCES `m_office` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

CREATE TABLE `ct_task_staff_mapping` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`task_id` BIGINT(20) NULL DEFAULT NULL,
	`staff_id` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_TaskID_TaskStaffMapping_TaskPlanner` (`task_id`),
	INDEX `FK_ct_task_staff_mapping_m_staff` (`staff_id`),
	CONSTRAINT `FK_ct_task_staff_mapping_m_staff` FOREIGN KEY (`staff_id`) REFERENCES `m_staff` (`id`),
	CONSTRAINT `FK_TaskID_TaskStaffMapping_TaskPlanner` FOREIGN KEY (`task_id`) REFERENCES `ct_task_planner` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;


CREATE TABLE `ct_client_attendence` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`task_id` BIGINT(20) NOT NULL,
	`client_id` BIGINT(20) NOT NULL,
	`attendence_type` SMALLINT(5) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK1__ct_task_planner` (`task_id`),
	INDEX `FK1__m_client` (`client_id`),
	CONSTRAINT `FK1__ct_task_planner` FOREIGN KEY (`task_id`) REFERENCES `ct_task_planner` (`id`),
	CONSTRAINT `FK1__m_client` FOREIGN KEY (`client_id`) REFERENCES `m_client` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

CREATE TABLE `ct_staff_attendence` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`task_id` BIGINT(20) NOT NULL,
	`staff_id` BIGINT(20) NOT NULL,
	`attendence_type` SMALLINT(5) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK__ct_task_planner` (`task_id`),
	INDEX `FK_ct_staff_attendence_m_staff` (`staff_id`),
	CONSTRAINT `FK_ct_staff_attendence_m_staff` FOREIGN KEY (`staff_id`) REFERENCES `m_staff` (`id`),
	CONSTRAINT `FK__ct_task_planner` FOREIGN KEY (`task_id`) REFERENCES `ct_task_planner` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

ALTER TABLE `m_note`
	ADD COLUMN `task_id` BIGINT(20) NULL DEFAULT NULL AFTER `loan_transaction_id`,
	ADD CONSTRAINT `FK_m_note_ct_task_planner` FOREIGN KEY (`task_id`) REFERENCES `ct_task_planner` (`id`);

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'CREATE_TASKPLAN', 'CREATE', 'TASKPLAN', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'TASKPLAN_RESEDULE', 'RESEDULE', 'TASKPLAN', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'COMPLETE_TASKPLAN', 'TASKPLAN', 'COMPLETE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'CANCLE_TASKPLAN', 'TASKPLAN', 'CANCLE', 0);


INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('Client Task Types', 1);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `code_score`) 
VALUES ((select id from m_code where code_name like 'Client Task Types'), 'House Visit', '', 0, NULL);

INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('Group Task Types', 1);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `code_score`) 
VALUES ((select id from m_code where code_name like 'Group Task Types'), 'CGT', '', 0, NULL);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `code_score`) 
VALUES ((select id from m_code where code_name like 'Group Task Types'), 'GRT', '', 1, NULL);
	

	



