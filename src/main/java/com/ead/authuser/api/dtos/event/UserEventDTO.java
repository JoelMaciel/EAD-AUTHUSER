package com.ead.authuser.api.dtos.event;

import com.ead.authuser.api.dtos.response.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserEventDTO {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String userType;
    private String userStatus;
    private String cpf;
    private String phoneNumber;
    private String imageUrl;
    private String actionType;

    public static UserEventDTO toUserEvent(UserDTO userDTO) {
        return UserEventDTO.builder()
                .userId(userDTO.getUserId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .fullName(userDTO.getFullName())
                .userType(userDTO.getUserType().toString())
                .userStatus(userDTO.getUserStatus().toString())
                .cpf(userDTO.getCpf())
                .phoneNumber(userDTO.getPhoneNumber())
                .imageUrl(userDTO.getImageUrl())
                .build();
    }
}
