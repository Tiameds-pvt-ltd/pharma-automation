package com.pharma.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyLoggedInException extends RuntimeException {

    public UserAlreadyLoggedInException() {
        super("User already logged in on another screen");
    }
}

