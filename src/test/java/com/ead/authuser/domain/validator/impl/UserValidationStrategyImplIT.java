package com.ead.authuser.domain.validator.impl;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.exceptions.CpflAlreadyExistsException;
import com.ead.authuser.domain.exceptions.EmailAlreadyExistsException;
import com.ead.authuser.domain.exceptions.UsernameAlreadyExistsException;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.util.UserObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class UserValidationStrategyImplIT {

    @Autowired
    private UserValidationStrategyImpl userValidationStrategy;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Given unique user data, When validate, Then no exceptions should be thrown")
    void givenUniqueUserData_WhenValidate_ThenNoExceptionsThrown() {
        UserRequest userRequest = UserObjectFactory.createUserRequest(
                "mirela", "new@example.com", "12345678910",
                "123456", "Mirela de Alencar", "8881148", "http://test.com/new_image.jpg");
        assertDoesNotThrow(() -> userValidationStrategy.validate(userRequest));
    }

    @Test
    @DisplayName("Given Existing Username, When validate, Then a UsernameAlreadyExistsException should be thrown")
    void givenExistingUsername_WhenValidate_ThenThrowException() {
        String username = "RachadaLuana123";
        UserRequest userRequest = UserObjectFactory.createUserRequest(
                username, "joel123@example.com", "12345678911",
                "123456", "Joel Maciel", "999999999", "http://test.com/image.jpg");
        userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UserRequest newUserRequest = UserObjectFactory.createUserRequest(
                username, "new2@example.com", "12345678912",
                "123456", "New User", "81118888", "http://test.com/new_image2.jpg");
        assertThrows(UsernameAlreadyExistsException.class, () -> userValidationStrategy.validate(newUserRequest));
    }

    @Test
    @DisplayName("Given Existing Email, When Validate, Then an EmailAlreadyExistsException Should Be Thrown")
    void givenExistingEmail_WhenValidate_ThenThrowException() {
        String email = "joel1234@example.com";
        UserRequest userRequest = UserObjectFactory.createUserRequest(
                "mateus123", email, "12345678913",
                "123456", "Mateus Lucas", "88825888", "http://test.com/new_image3.jpg");
        userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UserRequest newUserRequest = UserObjectFactory.createUserRequest(
                "mateus124", email, "12345678914",
                "123456", "Mateus Lucas 2", "88825888", "http://test.com/new_image4.jpg");
        assertThrows(EmailAlreadyExistsException.class, () -> userValidationStrategy.validate(newUserRequest));
    }

    @Test
    @DisplayName("Given Existing CPF, When Validate, Then a CPFlAlreadyExistsException should be thrown")
    void givenExistingCPF_WhenValidate_ThenThrowException() {
        String cpf = "12345678915";
        UserRequest userRequest = UserObjectFactory.createUserRequest(
                "Barbie123", "new3@example.com", cpf,
                "123456", "Barbie", "88887888", "http://test.com/new_image5.jpg");
        userRepository.save(UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT));

        UserRequest newUserRequest = UserObjectFactory.createUserRequest(
                "Barbie124", "new4@example.com", cpf,
                "123456", "Barbie 2", "88887889", "http://test.com/new_image6.jpg");
        assertThrows(CpflAlreadyExistsException.class, () -> userValidationStrategy.validate(newUserRequest));
    }
}
