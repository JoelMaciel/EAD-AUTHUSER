package com.ead.authuser.api.controller;

import com.ead.authuser.api.dtos.request.InstructorRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructors")
public class InstructorController {

    private final UserService userService;

    @PostMapping("/subscription")
    public UserDTO saveSubscriptionInstructor(@RequestBody @Valid InstructorRequest instructorRequest) {
        return userService.saveInstructor(instructorRequest);
    }
}
