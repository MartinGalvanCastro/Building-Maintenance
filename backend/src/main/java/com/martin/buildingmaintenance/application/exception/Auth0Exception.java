package com.martin.buildingmaintenance.application.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public abstract class  Auth0Exception extends BaseAppException {
    public Auth0Exception(String message) {
        super(message);
    }
}
