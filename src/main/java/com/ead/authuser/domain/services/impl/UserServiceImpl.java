package com.ead.authuser.domain.services.impl;

import com.ead.authuser.api.dtos.request.UpdateImage;
import com.ead.authuser.api.dtos.request.UpdatePassword;
import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.request.UserUpdateRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.exceptions.InvalidPasswordException;
import com.ead.authuser.domain.exceptions.UserNotFoundException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.UserService;
import com.ead.authuser.domain.validator.UserValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional
    @Override
    public UserDTO update(UUID userId, UserUpdateRequest userRequest) {
        User user = searchById(userId).toBuilder()
                .cpf(userRequest.getCpf())
                .fullName(userRequest.getFullName())
                .phoneNumber(userRequest.getPhoneNumber())
                .build();

        return UserDTO.toDTO(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDTO updatePassword(UUID userId, UpdatePassword updatePassword) {
        User user = validateUserPassword(userId, updatePassword);
        return UserDTO.toDTO(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDTO updateImagem(UUID userId, UpdateImage updateImage) {
        User user = searchById(userId).toBuilder()
                .imageUrl(updateImage.getImageUrl()).build();

        return UserDTO.toDTO(userRepository.save(user));
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        searchById(userId);
        userRepository.deleteById(userId);
    }


    private User validateUserPassword(UUID userId, UpdatePassword updatePassword) {
        User user = searchById(userId);
        if (!user.getPassword().equals(updatePassword.getOldPassword())) {
            throw new InvalidPasswordException(updatePassword.getOldPassword());
        }
        return user.toBuilder().password(updatePassword.getPassword()).build();
    }

    public User searchById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
