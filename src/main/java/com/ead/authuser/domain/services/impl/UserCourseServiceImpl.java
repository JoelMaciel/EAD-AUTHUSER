package com.ead.authuser.domain.services.impl;

import com.ead.authuser.api.dtos.request.UserCourseRequest;
import com.ead.authuser.api.dtos.response.UserCourseDTO;
import com.ead.authuser.domain.exceptions.ExistsUserAndCourseIdException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.models.UserCourse;
import com.ead.authuser.domain.repositories.UserCourseRepository;
import com.ead.authuser.domain.services.UserCourseService;
import com.ead.authuser.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;
    private final UserService userService;

    @Transactional
    @Override
    public UserCourseDTO save(UUID userId, UserCourseRequest userCourseRequest) {
        User user = userService.searchById(userId);
        if (existsByUserAndCourseId(user, userCourseRequest.getCourseId())) {
            throw new ExistsUserAndCourseIdException();
        }

        UserCourse userCourse = UserCourseRequest.toEntity(user, userCourseRequest.getCourseId());
        return UserCourseDTO.toDTO(userCourseRepository.save(userCourse));
    }

    @Override
    public boolean existsByUserAndCourseId(User user, UUID courseId) {
        return userCourseRepository.existsByUserAndCourseId(user, courseId);
    }
}
