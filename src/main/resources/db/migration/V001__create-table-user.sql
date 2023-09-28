CREATE TABLE `User` (
    `userId` CHAR(36) NOT NULL PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `user_status` ENUM('ACTIVE', 'BLOCKED') NOT NULL,
    `user_type` ENUM('ADMIN', 'STUDENT', 'INSTRUCTOR') NOT NULL,
    `phone_number` VARCHAR(20) NULL,
    `cpf` VARCHAR(11) NOT NULL UNIQUE,
    `creation_date` DATETIME NOT NULL,
    `update_date` DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
