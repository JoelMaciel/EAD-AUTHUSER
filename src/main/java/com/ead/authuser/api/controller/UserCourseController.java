package com.ead.authuser.api.controller;

import com.ead.authuser.api.dtos.response.CourseDTO;
import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;

    @GetMapping("api/users/{userId}/courses")
    public Page<CourseDTO> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId",
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable(value = "userId") UUID userId) {
        userService.searchById(userId);
        return courseClient.getAllCoursesByUser(userId, pageable);
    }
}
