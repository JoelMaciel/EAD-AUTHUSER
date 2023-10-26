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
import com.ead.authuser.domain.validator.UserValidationStrategy;
import com.ead.authuser.util.UserObjectFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplIT {

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
    @Disabled
    @DisplayName("Given Page of User When findAll is Invoked, Then should return a non-empty Page of Users.")
    void whenFindAll_thenReturnPageOfUsers() {
        UUID courseId = UUID.fromString(("70754308-6ba1-469c-8de8-c3e7e28dc404"));
        UserRequest userRequest = UserObjectFactory.createUserRequest("macieljoel", "joel@gmail.com", "628567838",
                "123456", "Joel Maciel", "999999999", "http://test.com/image.jpg");
        User user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);
        userRepository.save(user);

        UserRequest userRequest2 = UserObjectFactory.createUserRequest(
                "mariajose", "maria@gmail.com", "65856783388", "654321",
                "Maria Jose", "888888888", "http://test.com/image2.jpg");
        User user2 = UserObjectFactory.convertToUser(userRequest2, UserStatus.ACTIVE, UserType.ADMIN);
        userRepository.save(user2);

        Pageable pageable = PageRequest.of(0, 10);
        Specification<User> spec = null;

        int numbersUsers = 1;
        Page<UserDTO> resultPage = userService.findAll(spec, pageable, courseId);
        assertFalse(resultPage.isEmpty());
        assertEquals(numbersUsers, resultPage.getTotalElements());
    }

    @Test
    @DisplayName("Giver UserId When findById is invoked Then Should Return the Corresponding User.")
    void givenUserIdCorrect_whenFindById_ThenReturnUser() {
        UUID userId = UUID.fromString("99735306-994d-46f9-82a7-4116145a5678");

        assertNotNull(userService.findById(userId));

    }

    @Test
    @DisplayName("Given a non-existent user ID When calling FindById method Then you must throw Exception")
    void givenANonExistsUserId_WhenCallingFindByIdMethod_ThenYouMustThrowException() {
        UUID userId = UUID.randomUUID();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
        assertEquals(exception.getMessage(), "There is no code user " + userId + " in the database.");

    }

    @Test
    @DisplayName("Giver UserId Correct When a User is deleted Then the Total User Count Should Decrease By One.")
    void givenUserIdCorrect_whenDeleteUser_ThenShouldDecreaseByOneUser() {
        UUID userId = UUID.fromString("3106c73c-5142-480b-8344-388610678971");
        UUID courseId = UUID.fromString(("70754308-6ba1-469c-8de8-c3e7e28dc404"));

        PageRequest pageable = PageRequest.of(0, 10);
        Specification<User> spec = null;
        long originalSize = userService.findAll(spec, pageable, courseId).getTotalElements();

        userService.delete(userId);
        long newSize = userService.findAll(spec, pageable, courseId).getTotalElements();

        assertEquals(originalSize - 1, newSize);
    }

    @Test
    @DisplayName("Given a non-existent user ID When calling delete method Then you must throw Exception")
    void givenANonExistsUserId_WhenCallingDeleteMethod_ThenShouldThrowException() {
        UUID userId = UUID.randomUUID();

        UserNotFoundException except = assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        assertEquals(except.getMessage(), "There is no code user " + userId + " in the database.");
    }

    @Test
    @DisplayName("Giver UserRequest Valid When calling Save Method Then Should Return User with Successfully")
    void givenUserRequestValid_WhenSavedUser_ThenReturnSaveSuccessfully() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("macielviana", "maciel@gmail.com",
                "654", "123456", "Maria Silva", "777777777", "http://test.com/image2.jpg");
        userValidationStrategy.validate(userRequest);
        User user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);

        UserDTO userDTO = userService.save(userRequest);
        assertNotNull(userService.findById(userDTO.getUserId()));
        assertThat(userDTO).isNotNull();

    }

    @Test
    @DisplayName("Given valid UserUpdateRequest, When Update is called, Then user details should be updated successfully.")
    void givenValidUserUpdateRequest_WhenUpdate_ThenUserDetailsShouldUpdateSuccessfully() {
        UserRequest userRequest = UserObjectFactory.createUserRequest(
                "testuser", "testuser@gmail.com", "12345678901",
                "123456", "Test User", "999999990", "http://test.com/testuser_image.jpg");
        User user = userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UserUpdateRequest updateUserRequest = UserUpdateRequest.builder()
                .cpf("12345678902")
                .fullName("joelLucas")
                .phoneNumber("08598954512")
                .build();

        UserDTO userDTO = userService.update(user.getUserId(), updateUserRequest);

        assertNotNull(userDTO);
        assertEquals(updateUserRequest.getCpf(), userDTO.getCpf());
        assertEquals(updateUserRequest.getFullName(), userDTO.getFullName());
        assertEquals(updateUserRequest.getPhoneNumber(), userDTO.getPhoneNumber());


    }

    @Test
    @DisplayName("Given a valid old password, When updatePassword is called, Then password should be updated successfully.")
    void givenValidOldPassword_WhenUpdatePassword_ThenPasswordShouldUpdateSuccessfully() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("joel", "joeltest@gmail.com",
                "658567838", "123456", "Joel Maciel", "999999999",
                "http://test.com/image.jpg");

        User user = userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UpdatePassword updatePassword = new UpdatePassword("12345678", "123456");
        UserDTO userDTO = userService.updatePassword(user.getUserId(), updatePassword);

        assertNotNull(userDTO);
        assertEquals(user.getPassword(), updatePassword.getOldPassword());
    }

    @Test
    @DisplayName("Given an invalid old password, When updatePassword is called, Then throw InvalidPasswordException.")
    void givenInvalidOldPassword_WhenUpdatePassword_ThenThrowException() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("joelviana", "joeltest2@gmail.com",
                "658567839", "123456", "Joel Maciel",
                "999999998", "http://test.com/image2.jpg");
        User user = userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UpdatePassword updatePassword = new UpdatePassword("12345678", "99999");

        assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(user.getUserId(), updatePassword));
    }

    @Test
    @DisplayName("Given a new image URL, When updateImage is called, Then image should be updated successfully.")
    void givenNewImageURL_WhenUpdateImage_ThenImageShouldUpdateSuccessfully() {
        UserRequest userRequest = UserObjectFactory.createUserRequest("joelmacielviana", "joeltest3@gmail.com", "658567837",
                "123456", "Joel Maciel", "999999997", "http://test.com/image3.jpg");
        User createdUser = userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UpdateImage updateImage = new UpdateImage("http://new-image-url.com");
        UserDTO userDTO = userService.updateImage(createdUser.getUserId(), updateImage);

        assertNotNull(userDTO);
        assertEquals(userDTO.getImageUrl(), updateImage.getImageUrl());
    }

    @Test
    @DisplayName("Given an invalid userId, When updateImage is called, Then throw UserNotFoundException.")
    void givenInvalidUserId_whenUpdateImage_thenThrowException() {
        UUID invalidUserId = UUID.randomUUID();
        UpdateImage updateImage = new UpdateImage("http://new-image-url2.com");

        assertThrows(UserNotFoundException.class, () -> userService.updateImage(invalidUserId, updateImage));
    }

}
