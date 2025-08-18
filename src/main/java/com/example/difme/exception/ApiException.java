
package com.example.difme.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus code;
    private final int status;

    public ApiException(HttpStatus code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public HttpStatus getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}