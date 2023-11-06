package com.ead.authuser.domain.services;

import com.ead.authuser.api.dtos.request.UserCourseRequest;
import com.ead.authuser.api.dtos.response.UserCourseDTO;
import com.ead.authuser.domain.models.User;

import java.util.UUID;

public interface UserCourseService {

    UserCourseDTO save(UUID userId, UserCourseRequest userCourseRequest);

    boolean existsByUserAndCourseId(User user, UUID courseId);

    boolean existsByCourseId(UUID courseId);

    void delete(UUID courseId);
}