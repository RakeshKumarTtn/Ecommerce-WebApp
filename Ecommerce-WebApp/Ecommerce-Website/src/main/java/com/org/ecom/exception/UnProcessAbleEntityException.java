package com.org.ecom.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnProcessAbleEntityException extends RuntimeException {
    public UnProcessAbleEntityException(Throwable cause) {
        super(cause);
    }
}
