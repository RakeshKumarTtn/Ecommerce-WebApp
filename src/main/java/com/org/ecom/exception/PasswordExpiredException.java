package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PasswordExpiredException extends RuntimeException {
    public PasswordExpiredException(String s) {
        super(s);
    }
}
