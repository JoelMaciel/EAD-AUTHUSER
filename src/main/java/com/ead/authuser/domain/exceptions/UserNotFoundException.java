package com.ead.authuser.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends EntityNotExistsException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(UUID userId) {
        this(String.format("There is no code user %s in the database.", userId));
    }
}
