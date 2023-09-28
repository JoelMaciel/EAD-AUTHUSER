package com.ead.authuser.api.dtos.request;

import com.ead.authuser.domain.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String email;

    @CPF
    @NotNull
    private String cpf;

    @NotBlank
    private String password;
    private String oldPassword;

    @NotBlank
    private String fullName;
    private String phoneNumber;
    private String imageUrl;

    public static User toEntity(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .cpf(request.getCpf())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .imageUrl(request.getImageUrl())
                .build();
    }


}
