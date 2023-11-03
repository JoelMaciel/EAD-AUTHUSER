package com.ead.authuser.api.dtos.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class InstructorRequest {

    @NotNull
    private UUID userId;
}
