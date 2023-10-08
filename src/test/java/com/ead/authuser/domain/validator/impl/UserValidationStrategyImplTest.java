package com.ead.authuser.domain.validator.impl;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.domain.exceptions.CpflAlreadyExistsException;
import com.ead.authuser.domain.exceptions.EmailAlreadyExistsException;
import com.ead.authuser.domain.exceptions.UsernameAlreadyExistsException;
import com.ead.authuser.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationStrategyImplTest {

    @InjectMocks
    private UserValidationStrategyImpl userValidationStrategy;

    @Mock
    private UserRepository userRepository;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest = UserRequest.builder()
                .username("joelmaciel")
                .email("joel@mail.com")
                .cpf("123456789")
                .build();
    }

    @Test
    @DisplayName("Given User When Validate Then Must validate User Successfuly")
    void givenUser_WhenValidate_ThenMustValidateUserSuccessfuly() {
        when(userRepository.existsByCpf(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> userValidationStrategy.validate(userRequest));
    }

    @Test
    @DisplayName("Given User With a Name Already Exists When To Validate Then Must Throw an Exception")
    void givenUserWithNameThatAlreadyExists_WhenValidate_ThenMustThrowException() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userValidationStrategy.validate(userRequest));
    }

    @Test
    @DisplayName("Given User With a Email Already Exists When To Validate Then Must Throw an Exception")
    void givenUserWithEmailThatAlreadyExists_WhenValidate_ThenMustThrowException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userValidationStrategy.validate(userRequest));
    }

    @Test
    @DisplayName("Given User With a CPF Already Exists When To Validate Then Must Throw an Exception")
    void givenUserWithCPFThatAlreadyExists_WhenValidate_ThenMustThrowException() {
        when(userRepository.existsByCpf(anyString())).thenReturn(true);

        assertThrows(CpflAlreadyExistsException.class, () -> userValidationStrategy.validate(userRequest));
    }
}
