package com.ead.authuser.domain.exceptions;

public class CpflAlreadyExistsException extends EntityInUseException {

    public CpflAlreadyExistsException(String cpf) {
        super(String.format("There is already a registered user with this cpf %s", cpf));
    }
}
