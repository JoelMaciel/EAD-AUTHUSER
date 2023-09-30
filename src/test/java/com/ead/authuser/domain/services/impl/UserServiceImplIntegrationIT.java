package com.ead.authuser.domain.services.impl;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.response.UserDTO;
import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.exceptions.UserNotFoundException;
import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.validator.UserValidationStrategy;
import com.ead.authuser.util.UserObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplIntegrationIT {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserValidationStrategy userValidationStrategy;
    @Autowired
    private UserRepository userRepository;
    private UserRequest userRequest;
    private User user;
    private User user2;

    @Test
    @DisplayName("When findAll is invoked, it should return a non-empty list of users.")
    void whenFindAll_thenReturnListOfUsers() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("macieljoel", "joel@gmail.com", "658567838",
                "123456", "Joel Maciel", "999999999", "http://test.com/image.jpg");
        User user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);
        userRepository.save(user);

        UserRequest userRequest2 = UserObjectFactory.createUserRequest(
                "mariajose", "maria@gmail.com", "65856783388", "654321",
                "Maria Jose", "888888888", "http://test.com/image2.jpg");
        User user2 = UserObjectFactory.convertToUser(userRequest2, UserStatus.ACTIVE, UserType.ADMIN);
        userRepository.save(user2);

        int numbersUsers = 7;
        assertFalse(userService.findAll().isEmpty());
        assertEquals(numbersUsers, userService.findAll().size());
    }

    @Test
    @DisplayName("When findById is invoked with a valid ID, it should return the corresponding user.")
    void whenFindById_thenReturnUser() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("joelmaciel", "joel1@gmail.com", "8957998",
                "123456", "Joel Maciel", "999999999", "http://test.com/image.jpg");
        User user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);
        userRepository.save(user);

        assertNotNull(userService.findById(user.getUserId()));
    }

    @Test
    @DisplayName("Given a non-existent user ID When calling FindById method Then you must throw Exception")
    void giverANonExistsUserId_WhenCallingFindByIdMethod_ThenYouMustThrowException() {
        UUID userId = UUID.randomUUID();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        assertEquals(exception.getMessage(), "There is no code user " + userId + " in the database.");

    }

    @Test
    @DisplayName("When a user is deleted, the total user count should decrease by one.")
    void whenDelete_thenUserShouldBeDeleted() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("joelviana", "joel2@gmail.com", "658567837",
                "123456", "Joel Maciel", "999999999", "http://test.com/image.jpg");
        User user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);
        userRepository.save(user);

        int originalSize = userService.findAll().size();

        userService.delete(user.getUserId());
        int newSize = userService.findAll().size();

        assertEquals(originalSize - 1, newSize);
    }

    @Test
    @DisplayName("Given a non-existent user ID When calling delete method Then you must throw Exception")
    void giverANonExistsUserIdW_henCallingDeleteMethod_ThenYouMustThrowException() {
        UUID userId = UUID.randomUUID();

        UserNotFoundException except = assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        assertEquals(except.getMessage(), "There is no code user " + userId + " in the database.");
    }

    @Test
    @DisplayName("When calling save method Then it should return User with Success")
    void whenSave_thenReturnSavedUser() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("macielviana", "maciel@gmail.com",
                "654", "123456", "Maria Silva", "777777777", "http://test.com/image2.jpg");
        userValidationStrategy.validate(userRequest);
        User user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);

        UserDTO userDTO = userService.save(userRequest);
        assertNotNull(userService.findById(userDTO.getUserId()));
        assertThat(userDTO).isNotNull();

    }
}
