set foreign_key_checks = 0;

delete from user;
delete from user_course;

set foreign_key_checks = 1;

INSERT INTO `user` (
  `user_id`, `username`, `email`, `password`, `full_name`,
  `user_status`, `user_type`, `phone_number`, `cpf`,
  `creation_date`, `update_date`, `image_url`
) VALUES
  (
    '3106c73c-5142-480b-8344-388610678971', 'johnDoe', 'john.doe@example.com', '123456', 'John Doe',
    'ACTIVE', 'STUDENT', '085999358547', '26555930063',
    NOW(), NOW(), 'http://example.com/image1.jpg'
  ),
  (
    '99735306-994d-46f9-82a7-4116145a5678', 'aliceSmith', 'alice.smith@example.com', '123456', 'Alice Smith',
    'BLOCKED', 'INSTRUCTOR', '081927654321', '38580865093',
    NOW(), NOW(), 'http://example.com/image2.jpg'
  ),
  (
   'c85685f2-4135-492d-873c-936471e60792', 'bobJones', 'bob.jones@example.com', '123456', 'Bob Jones',
    'ACTIVE', 'ADMIN', '088989541245', '59311079081',
    NOW(), NOW(), 'http://example.com/image3.jpg'
  );

  INSERT INTO `user_course` (`id`, `user_id`, `course_id`) VALUES
  ('1a2b3c4d-5e6f-7a8b-9c0d-1e2f3g4h5i6j', '3106c73c-5142-480b-8344-388610678971', '70754308-6ba1-469c-8de8-c3e7e28dc404'),
  ('c9805b25-ab2f-404b-a3a4-e518656d131e', '99735306-994d-46f9-82a7-4116145a5678', '9e9deb7c-6763-11ee-8c99-0242ac120002');

