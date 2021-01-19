CREATE SCHEMA `taxi_service` DEFAULT CHARACTER SET utf8;

CREATE TABLE `taxi_service`.`manufacturers` (
        `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
        `name` VARCHAR(225) NOT NULL,
        `country` VARCHAR(225) NOT NULL,
        `deleted` TINYINT NOT NULL DEFAULT 0,
        PRIMARY KEY (`id`));
CREATE TABLE `taxi_service`.`drivers` (
        `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
        `name` VARCHAR(225) NOT NULL,
        `license_number` VARCHAR(225) NOT NULL,
        `deleted` TINYINT NOT NULL DEFAULT 0,
        PRIMARY KEY (`id`));
CREATE TABLE `taxi_service`.`cars` (
        `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
        `manufacturer_id` BIGINT(11) NOT NULL,
        `model` VARCHAR(225) NOT NULL,
        `deleted` TINYINT NOT NULL DEFAULT 0,
        PRIMARY KEY (`id`),
        INDEX `cars_manufacturers_fk_index` (`manufacturer_id` ASC) VISIBLE,
        CONSTRAINT `cars_manufacturers_fk`
        FOREIGN KEY (`manufacturer_id`)
        REFERENCES `taxi_service`.`manufacturers` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION);
CREATE TABLE `taxi_service`.`cars_drivers` (
        `driver_id` BIGINT(11) NOT NULL,
        `car_id` BIGINT(11) NOT NULL,
        INDEX `cd_cars_fk_index` (`car_id` ASC) VISIBLE,
        CONSTRAINT `cd_drivers_fk`
        FOREIGN KEY (`driver_id`)
        REFERENCES `taxi_service`.`drivers` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
        CONSTRAINT `cd_cars_fk`
        FOREIGN KEY (`car_id`)
        REFERENCES `taxi_service`.`cars` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION);
