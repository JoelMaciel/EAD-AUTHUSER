package com.ead.authuser.domain.exceptions;

public class UsernameAlreadyExistsException extends EntityInUseException {

    public UsernameAlreadyExistsException(String username) {
        super(String.format("There is already a registered user with this username %s", username));
    }
}
