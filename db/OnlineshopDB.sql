-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema onlineshop
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema onlineshop
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `onlineshop` DEFAULT CHARACTER SET utf8 ;
USE `onlineshop` ;

-- -----------------------------------------------------
-- Table `onlineshop`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`users` (
  `user_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(20) NOT NULL,
  `lastname` VARCHAR(20) NOT NULL,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(65) NOT NULL,
  `phone` VARCHAR(15) NULL DEFAULT NULL,
  `address` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(40) NOT NULL,
  `confirmation_status` TINYINT(1) NOT NULL,
  `access_privilege` ENUM('user', 'manager') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 19
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`addresses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`addresses` (
  `address_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `shipping_address` VARCHAR(100) NOT NULL,
  `user_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`address_id`),
  INDEX `user_id_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_id_addresses`
    FOREIGN KEY (`user_id`)
    REFERENCES `onlineshop`.`users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`baskets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`baskets` (
  `basket_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `total_price` DOUBLE NOT NULL,
  `user_id` INT(10) UNSIGNED NOT NULL,
  `basket_status` ENUM('current', 'saled') NULL DEFAULT NULL,
  PRIMARY KEY (`basket_id`),
  INDEX `fk_baskets_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_baskets_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `onlineshop`.`users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 34
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`categories` (
  `category_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`creditcards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`creditcards` (
  `card_id` INT(10) UNSIGNED NOT NULL,
  `billing_address` VARCHAR(45) NOT NULL,
  `balance` DOUBLE NOT NULL,
  PRIMARY KEY (`card_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`products` (
  `product_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` DOUBLE UNSIGNED NOT NULL,
  `description` TEXT NOT NULL,
  `shipping price` DOUBLE NOT NULL,
  `quantity` INT(10) NOT NULL,
  `category_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `fk_category_id_products_idx` (`category_id` ASC),
  CONSTRAINT `fk_category_id_products`
    FOREIGN KEY (`category_id`)
    REFERENCES `onlineshop`.`categories` (`category_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`medias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`medias` (
  `media_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `media_path` VARCHAR(250) NOT NULL,
  `product_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`media_id`),
  INDEX `product_id_idx` (`product_id` ASC),
  CONSTRAINT `fk_product_id_medias`
    FOREIGN KEY (`product_id`)
    REFERENCES `onlineshop`.`products` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`orderitems`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`orderitems` (
  `orderitem_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `basket_id` INT(10) UNSIGNED NOT NULL,
  `product_id` INT(10) UNSIGNED NOT NULL,
  `quantity` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`orderitem_id`),
  INDEX `product_id_idx` (`product_id` ASC),
  INDEX `basket_id_idx` (`basket_id` ASC),
  CONSTRAINT `fk_basket_id_orderitems`
    FOREIGN KEY (`basket_id`)
    REFERENCES `onlineshop`.`baskets` (`basket_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_product_id_orderitems`
    FOREIGN KEY (`product_id`)
    REFERENCES `onlineshop`.`products` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`sales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`sales` (
  `sale_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) UNSIGNED NOT NULL,
  `date_of_purchuase` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `card_id` INT(10) UNSIGNED NOT NULL,
  `address_id` INT(10) UNSIGNED NOT NULL,
  `basket_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`sale_id`),
  UNIQUE INDEX `basket_id_UNIQUE` (`basket_id` ASC),
  INDEX `user_id_idx` (`user_id` ASC),
  INDEX `card_id_idx` (`card_id` ASC),
  INDEX `address_id_idx` (`address_id` ASC),
  INDEX `fk_sales_baskets1_idx` (`basket_id` ASC),
  CONSTRAINT `fk_address_id_sales`
    FOREIGN KEY (`address_id`)
    REFERENCES `onlineshop`.`addresses` (`address_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_basket_id_sales`
    FOREIGN KEY (`basket_id`)
    REFERENCES `onlineshop`.`baskets` (`basket_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_cardid_sales`
    FOREIGN KEY (`card_id`)
    REFERENCES `onlineshop`.`creditcards` (`card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id_sales`
    FOREIGN KEY (`user_id`)
    REFERENCES `onlineshop`.`users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `onlineshop`.`wishlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlineshop`.`wishlist` (
  `user_id` INT(10) UNSIGNED NOT NULL,
  `product_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `product_id`),
  INDEX `user_id_idx` (`product_id` ASC),
  INDEX `product_id_idx` (`user_id` ASC),
  CONSTRAINT `fk_product_id_wishlist`
    FOREIGN KEY (`product_id`)
    REFERENCES `onlineshop`.`products` (`product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id_wishlist`
    FOREIGN KEY (`user_id`)
    REFERENCES `onlineshop`.`users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
