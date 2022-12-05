package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class EmailOrUsernameAlreadyExists extends RuntimeException {
    public EmailOrUsernameAlreadyExists(String s) {
        super(s);
    }
}
