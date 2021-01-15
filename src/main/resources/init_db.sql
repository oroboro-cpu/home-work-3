CREATE SCHEMA `taxi_service` DEFAULT CHARACTER SET utf8;

CREATE TABLE `taxi_service`.`manufacturers` (
        `manufacturer_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
        `manufacturer_name` VARCHAR(225) NOT NULL,
        `manufacturer_country` VARCHAR(225) NOT NULL,
        `deleted` TINYINT NOT NULL DEFAULT 0,
        PRIMARY KEY (`manufacturer_id`));
