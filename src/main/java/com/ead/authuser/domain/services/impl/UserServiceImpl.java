package com.ead.authuser.domain.services.impl;

import com.ead.authuser.api.controller.UserController;
import com.ead.authuser.api.dtos.request.*;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.api.specification.SpecificationTemplate;
import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.exceptions.InvalidPasswordException;
import com.ead.authuser.domain.exceptions.UserNotFoundException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.services.UserService;
import com.ead.authuser.domain.validator.UserValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidationStrategy userValidationStrategy;

    @Override
    public Page<UserDTO> findAll(Specification<User> spec, Pageable pageable, UUID courseId) {
        Page<User> usersPage = null;
        if (courseId != null) {
            usersPage = findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable);
        } else {
            usersPage = userRepository.findAll(spec, pageable);
        }
        addHateoasLinks(usersPage);
        return usersPage.map(UserDTO::toDTO);
    }

    private Page<User> findAll(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
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

    @Override
    public UserDTO saveInstructor(InstructorRequest instructorRequest) {
        User user = searchById(instructorRequest.getUserId()).toBuilder()
                .userType(UserType.INSTRUCTOR)
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
    public UserDTO updateImage(UUID userId, UpdateImage updateImage) {
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

    private void addHateoasLinks(Page<User> users) {
        if (!users.isEmpty()) {
            for (User user : users) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
    }

    @Override
    public User searchById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}
