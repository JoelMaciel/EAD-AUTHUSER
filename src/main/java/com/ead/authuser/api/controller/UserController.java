package com.ead.authuser.api.controller;

import com.ead.authuser.api.dtos.request.UpdateImage;
import com.ead.authuser.api.dtos.request.UpdatePassword;
import com.ead.authuser.api.dtos.request.UserUpdateRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.api.specification.SpecificationTemplate;
import com.ead.authuser.domain.services.UserService;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<UserDTO> getAllUsers(SpecificationTemplate.UserSpec spec, @PageableDefault(page = 0, size = 10, sort = "userId",
            direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.findAll(spec, pageable);
    }

    @GetMapping("/{userId}")
    public UserDTO getOneUser(@PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable UUID userId, @Valid @RequestBody UserUpdateRequest userRequest) {
        return userService.update(userId, userRequest);
    }

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDTO updatePassword(@PathVariable UUID userId, @RequestBody @Valid UpdatePassword updatePassword) {
        return userService.updatePassword(userId, updatePassword);
    }

    @PutMapping("/{userId}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDTO updateImage(@PathVariable UUID userId, @RequestBody @Valid UpdateImage updateImage) {
        return userService.updateImage(userId, updateImage);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
    }
}
