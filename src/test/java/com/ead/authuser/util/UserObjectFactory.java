package com.ead.authuser.util;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.api.dtos.request.UserUpdateRequest;
import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserObjectFactory {
    public static UserRequest createUserRequest(
            String username, String email, String cpf, String password, String fullName, String phoneNumber, String imageUrl
    ) {
        return UserRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .cpf(cpf)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .imageUrl(imageUrl)
                .build();
    }

    public static User convertToUser(UserRequest userRequest, UserStatus userStatus, UserType userType) {
        return UserRequest.toEntity(userRequest).toBuilder()
                .userStatus(userStatus)
                .userType(userType)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    public static UserUpdateRequest createUserUpdateRequest(String cpf, String fullName, String phoneNumber) {
        return UserUpdateRequest.builder()
                .cpf(cpf)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .build();
    }

}
