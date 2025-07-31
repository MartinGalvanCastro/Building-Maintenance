package com.martin.buildingmaintenance.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends Auth0Exception {
    public AccessDeniedException(String message) {
        super(message);
    }
}
