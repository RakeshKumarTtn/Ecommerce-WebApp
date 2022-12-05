package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AlreadyLoggedInUserException extends RuntimeException{
    public AlreadyLoggedInUserException(String s) {
        super(s);
    }
}
