package com.ead.authuser.domain.services;

import com.ead.authuser.api.dtos.request.UpdateImage;
import com.ead.authuser.api.dtos.request.UpdatePassword;
import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.request.UserUpdateRequest;
import com.ead.authuser.api.dtos.response.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO findById(UUID userId);

    void delete(UUID userId);

    UserDTO save(UserRequest userRequest);

    UserDTO update(UUID userId, UserUpdateRequest userRequest);

    UserDTO updatePassword(UUID userId, UpdatePassword updatePassword);

    UserDTO updateImagem(UUID userId, UpdateImage updateImage);
}
