package com.ead.authuser.domain.repositories;

import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {

    boolean existsByUserAndCourseId(User user, UUID courseId);
    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}
