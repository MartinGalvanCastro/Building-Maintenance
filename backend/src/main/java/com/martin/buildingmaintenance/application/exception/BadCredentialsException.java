package com.martin.buildingmaintenance.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BadCredentialsException extends BaseAppException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
