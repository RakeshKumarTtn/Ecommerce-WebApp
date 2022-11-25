package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PatternMismatchException extends RuntimeException {
    public PatternMismatchException(String message) {
        super(message);
    }
}
