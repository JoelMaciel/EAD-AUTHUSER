package com.ead.authuser.api.controller;

import com.ead.authuser.api.dtos.request.UserCourseRequest;
import com.ead.authuser.api.dtos.response.CourseDTO;
import com.ead.authuser.api.dtos.response.UserCourseDTO;
import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.domain.services.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/{userId}/courses")
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserCourseService userCourseService;

    @GetMapping
    public Page<CourseDTO> getAllCoursesByUser(@PageableDefault(page = 0, size = 10, sort = "courseId",
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable(value = "userId") UUID userId) {
        return courseClient.getAllCoursesByUser(userId, pageable);
    }

    @PostMapping("/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCourseDTO saveSubscriptionUserInCourse(@PathVariable UUID userId,
                                   @RequestBody @Valid UserCourseRequest userCourseRequest) {
        return userCourseService.save(userId, userCourseRequest);
    }
}
