package com.ead.authuser.domain.validator;

import com.ead.authuser.api.dtos.request.UserRequest;

public interface UserValidationStrategy {
    void validate(UserRequest request);
}
