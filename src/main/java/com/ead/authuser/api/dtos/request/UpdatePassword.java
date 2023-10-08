package com.ead.authuser.api.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePassword {

    @NotBlank
    private String password;
    @NotBlank
    private String oldPassword;
}
