package com.org.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class IdenticalException extends RuntimeException {

    private static final long serialVersionUID = -556201974937511653L;

    public IdenticalException() {
        super();
    }

    public IdenticalException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdenticalException(String message) {
        super(message);
    }

    public IdenticalException(Throwable cause) {
        super(cause);
    }
}
