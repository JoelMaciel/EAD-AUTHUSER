package com.ead.authuser.domain.exceptions;

public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException(String password) {
        super(String.format("Error: Mismatched old password , %s", password));
    }
}
