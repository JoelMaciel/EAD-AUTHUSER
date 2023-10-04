package com.ead.authuser.api.controller;

import com.ead.authuser.api.dtos.request.UpdateImage;
import com.ead.authuser.api.dtos.request.UpdatePassword;
import com.ead.authuser.api.dtos.request.UserUpdateRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDTO getOneUser(@PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable UUID userId, @Valid @RequestBody  UserUpdateRequest userRequest) {
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
        return userService.updateImagem(userId, updateImage);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
    }
}
