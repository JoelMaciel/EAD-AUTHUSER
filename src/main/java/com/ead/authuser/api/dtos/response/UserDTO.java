package com.ead.authuser.api.dtos.response;

import com.ead.authuser.domain.enums.UserStatus;
import com.ead.authuser.domain.enums.UserType;
import com.ead.authuser.domain.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
public class UserDTO extends RepresentationModel<UserDTO> {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String cpf;
    private UserStatus userStatus;
    private UserType userType;
    private String phoneNumber;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime updateDate;

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .cpf(user.getCpf())
                .fullName(user.getFullName())
                .userStatus(user.getUserStatus())
                .userType(user.getUserType())
                .phoneNumber(user.getPhoneNumber())
                .imageUrl(user.getImageUrl())
                .creationDate(user.getCreationDate())
                .updateDate(user.getUpdateDate())
                .build();

        userDTO.add(user.getLinks());
        return userDTO;
    }
}
