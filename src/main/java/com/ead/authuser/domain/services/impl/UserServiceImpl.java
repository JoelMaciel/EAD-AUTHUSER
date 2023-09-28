package com.ead.authuser.domain.services.impl;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.exceptions.UserNotFoundException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.UserService;
import com.ead.authuser.domain.validator.UserValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidationStrategy userValidationStrategy;

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(UUID userId) {
        return UserDTO.toDTO(searchById(userId));
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        searchById(userId);
        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public UserDTO save(UserRequest userRequest) {
        userValidationStrategy.validate(userRequest);

            User user = UserRequest.toEntity(userRequest)
                    .toBuilder()
                    .userStatus(UserStatus.ACTIVE)
                    .userType(UserType.STUDENT)
                    .creationDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            return UserDTO.toDTO(userRepository.save(user));

    }

    public User searchById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
