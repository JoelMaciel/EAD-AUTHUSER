package com.ead.authuser.api.dtos.request;

import com.ead.authuser.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
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
