package com.martin.buildingmaintenance.application.exception;

public class EmailAlreadyExistsException extends BaseAppException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}

