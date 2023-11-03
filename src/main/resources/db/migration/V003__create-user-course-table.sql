CREATE TABLE `user_course` (
    `id` CHAR(36) NOT NULL PRIMARY KEY,
    `user_id` CHAR(36) NOT NULL,
    `course_id` CHAR(36) NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
);
