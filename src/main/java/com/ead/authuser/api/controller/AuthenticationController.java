package com.ead.authuser.api.controller;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.save(userRequest);
    }
}
