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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidationStrategy userValidationStrategy;

    private List<User> listMockUsers;
    private User user;
    private User user2;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {

        userRequest = UserObjectFactory.createUserRequest("joelmaciel", "joel@gmail.com", "65856783388",
                "123456", "Joel Maciel", "999999999", "http://test.com/image.jpg");

        user = UserObjectFactory.convertToUser(userRequest, UserStatus.ACTIVE, UserType.STUDENT);

        UserRequest userRequest2 = UserObjectFactory.createUserRequest(
                "mariajose", "maria@gmail.com", "65856783388", "654321",
                "Maria Jose", "888888888", "http://test.com/image2.jpg");

        user2 = UserObjectFactory.convertToUser(userRequest2, UserStatus.ACTIVE, UserType.ADMIN);

        listMockUsers = Arrays.asList(user, user2);

    }

    @Test
    @DisplayName("Given a UserRequest When Save Then It Should Return a UserDTO")
    void givenUserRequest_WhenSave_ThenReturnsUserDTO() {
        doNothing().when(userValidationStrategy).validate(any(UserRequest.class));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = userService.save(userRequest);

        assertNotNull(userDTO);
        assertEquals(user.getUserId(), userDTO.getUserId());
        assertEquals(userRequest.getUsername(), userDTO.getUsername());
        assertEquals(userRequest.getEmail(), userDTO.getEmail());
        verify(userValidationStrategy).validate(userRequest);
        verify(userRepository).save(user);
    }


    @Test
    @DisplayName("Given UserId When SearchingById Then It Must Return a UserDTO")
    void givenUserId_WhenSearchingById_ThenItMustReturnUserDTO() {
        UUID userId = user.getUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDTO userDTO = userService.findById(userId);

        assertNotNull(userDTO);
        assertEquals(user.getUserId(), userDTO.getUserId());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getEmail(), userDTO.getEmail());

        verify(userRepository).findById(userId);

    }

    @Test
    @DisplayName("Given Nonexistent UserId When FindById Then It Must Throw UserNotFoundException")
    void givenNonexistentUserId_WhenSearchingById_ThenItMustThrowUserNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, ()-> userService.findById(userId));
        verify(userRepository).findById(userId);


    }

    @Test
    @DisplayName("When FindAll is called Then It Must Return a List of UserDTOs")
    void findAll_ShouldReturnListOfUserDTOs() {
        when(userRepository.findAll()).thenReturn(listMockUsers);
        List<UserDTO> userDTOs = userService.findAll();

        assertNotNull(userDTOs);
        assertEquals(2, userDTOs.size());
        assertEquals(user.getUserId(), userDTOs.get(0).getUserId());
        assertEquals(user.getUsername(), userDTOs.get(0).getUsername());
        assertEquals(user.getUserType(), userDTOs.get(0).getUserType());

        assertEquals(user2.getUserId(), userDTOs.get(1).getUserId());
        assertEquals(user2.getUsername(), userDTOs.get(1).getUsername());
        assertEquals(user2.getUserType(), userDTOs.get(1).getUserType());

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Given a valid userId, it should call the repository's deleteById method")
    void givenValidUserId_whenDelete_thenCallRepositoryDeleteById() {
        UUID userId = user.getUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Given a non-existent user ID When Calling Delete method Then you Must Throw Exception")
    void givenInvalidUserId_whenDelete_thenThrowException() {

        UUID userId = user.getUserId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).deleteById(userId);

    }
}





