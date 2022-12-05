package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PasswordMatchException extends RuntimeException{
    public PasswordMatchException(String s) {
        super(s);
    }
}
