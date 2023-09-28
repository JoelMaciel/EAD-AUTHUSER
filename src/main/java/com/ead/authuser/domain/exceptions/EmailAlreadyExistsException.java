package com.ead.authuser.domain.exceptions;

public class EmailAlreadyExistsException extends EntityInUseException {

    public EmailAlreadyExistsException(String email) {
        super(String.format("There is already a registered user with this email %s", email));
    }
}
