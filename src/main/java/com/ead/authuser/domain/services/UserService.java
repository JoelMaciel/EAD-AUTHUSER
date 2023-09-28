package com.ead.authuser.domain.services;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.response.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO findById(UUID userId);

    void delete(UUID userId);

    UserDTO save(UserRequest userRequest);
}
