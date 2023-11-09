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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
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

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user = User.builder()
                .userId(UUID.randomUUID())
                .username("joelmaciel")
                .email("joel@example.com")
                .password("123456")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Given Valid UserId When FindById Then Must Return User Successfully")
    void givenValidUserId_WhenFindById_ThenMustReturnUserSuccessfully() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        assertNotNull(userService.findById(user.getUserId()));
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    @DisplayName("Given UserId Invalid When FindById Then Should Throw Exception")
    void givenUserIdInvalid_WhenFindById_ThenShouldThrowException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(user.getUserId()));
        verify(userRepository).findById(user.getUserId());
    }

    @Test
    @DisplayName("Given User Valid When Save User Then Should Save User Successfully")
    void givenUserValid_WhenSaveUser_ThenShouldSaveUserSuccessfully() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testUser");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertNotNull(userService.save(userRequest));
        verify(userRepository).save(any(User.class));
    }
    @Test
    @DisplayName("Given Specification and Pageable, When findAll is called, Then Should Return Page of Users")
    void givenSpecAndPageable_WhenFindAll_ThenReturnPageOfUsers() {
        UUID courseId = UUID.fromString(("70754308-6ba1-469c-8de8-c3e7e28dc404"));
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(userPage);

        Page<UserDTO> result = userService.findAll(null, pageable, courseId);

        assertNotNull(result);
        verify(userRepository).findAll(any(Specification.class), eq(pageable));
        assertEquals(1, result.getTotalElements());
    }



    @Test
    @DisplayName("Given Page of Users, When addHateoasLinks is called, Then Users should have self-rel link")
    void givenPageOfUsers_WhenAddHateoasLinks_ThenUsersShouldHaveSelfRelLink() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        Method method = UserServiceImpl.class.getDeclaredMethod("addHateoasLinks", Page.class);
        method.setAccessible(true);
        method.invoke(userService, userPage);

        for (User userInPage : userPage) {
            assertTrue(userInPage.hasLinks());
            assertTrue(userInPage.getLink("self").isPresent());
        }
    }


    @Test
    @DisplayName("Given User Valid When Update User Then Should Updated User Successfully")
    void givenUserValid_WhenUpdateUser_ThenShouldUpdatedUserSuccessfully() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setCpf("123456789");
        updateRequest.setFullName("Maciel Viana");
        updateRequest.setPhoneNumber("1234567890");

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertNotNull(userService.update(user.getUserId(), updateRequest));
        verify(userRepository).findById(user.getUserId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Disabled
    @DisplayName("Given User Valid When Delete User Then Should Deleted User Successfully")
    void givenUserValid_WhenDeleteUser_ThenShouldDeletedUserSuccessfully() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        userService.delete(user.getUserId());

        verify(userRepository).findById(user.getUserId());
        verify(userRepository).deleteById(user.getUserId());
    }

    @Test
    @DisplayName("Given User Does Not Exists When Delete User Then Throw Exception")
    void givenUserDoesNotExists_WhenDeleteUser_ThenThrowException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(user.getUserId()));

        verify(userRepository).findById(user.getUserId());
        verify(userRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("Given Old Password Valid When Update Password Then Should Update Password Successfully")
    void givenOldPasswordValid_WhenUpdatePassword_ThenShouldUpdatePasswordSuccessfully() {
        UpdatePassword updatePassword = new UpdatePassword("password999", "123456");

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertNotNull(userService.updatePassword(user.getUserId(), updatePassword));
        verify(userRepository).findById(user.getUserId());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Given User Not Found When Update Password Then Throw Exception")
    void givenUserIdNotFound_WhenUpdatePassword_ThenThrowException() {
        UpdatePassword updatePassword = new UpdatePassword("12345", "123546");
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(user.getUserId(), updatePassword));
        verify(userRepository).findById(user.getUserId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Given OldPassword invalid When Update Password Then Throw Exception")
    void givenOldPasswordInvalid_WhenUpdatePassword_ThenThrowException() {
        UpdatePassword updatePassword = new UpdatePassword("9999", "99999");

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(user.getUserId(), updatePassword));
        verify(userRepository).findById(user.getUserId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Given New Image When Update Image Then Should Update Image Successfully")
    void givenNewImage_WhenUpdateImageThenShouldUpdateImageSuccessfully() {
        UpdateImage updateImage = new UpdateImage("http://newImageLink.com");

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertNotNull(userService.updateImage(user.getUserId(), updateImage));
        verify(userRepository).findById(user.getUserId());
        verify(userRepository).save(user);

    }
}
