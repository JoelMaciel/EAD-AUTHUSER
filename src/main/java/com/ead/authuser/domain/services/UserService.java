package com.ead.authuser.domain.services;

import com.ead.authuser.api.dtos.request.UpdateImage;
import com.ead.authuser.api.dtos.request.UpdatePassword;
import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.request.UserUpdateRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface UserService {
    Page<UserDTO> findAll(Specification<User> spec, Pageable pageable, UUID courseId);

    UserDTO findById(UUID userId);

    void delete(UUID userId);

    UserDTO save(UserRequest userRequest);

    UserDTO update(UUID userId, UserUpdateRequest userRequest);

    UserDTO updatePassword(UUID userId, UpdatePassword updatePassword);

    UserDTO updateImage(UUID userId, UpdateImage updateImage);
    User searchById(UUID userId);
}
