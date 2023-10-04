package com.ead.authuser.api.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserUpdateRequest {

    @CPF
    @NotNull
    @Size(max = 11)
    private String cpf;
    @NotBlank
    private String fullName;
    @NotBlank
    private String phoneNumber;

}
