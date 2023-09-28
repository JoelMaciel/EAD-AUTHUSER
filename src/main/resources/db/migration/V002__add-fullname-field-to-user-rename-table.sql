RENAME TABLE `User` TO `user`;
ALTER TABLE `user` CHANGE `userId` `user_id` CHAR(36) NOT NULL;
ALTER TABLE `user` ADD `full_name` VARCHAR(150) NOT NULL AFTER `password`;
ALTER TABLE `user` ADD `image_url` VARCHAR(255) NULL;
