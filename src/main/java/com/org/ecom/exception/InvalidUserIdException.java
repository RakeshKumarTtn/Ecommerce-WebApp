package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidUserIdException extends RuntimeException{
    public InvalidUserIdException(String s) {
        super(s);
    }
}
