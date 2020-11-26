package com.softserveinc.ita.homeproject.service.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private HttpStatus status;

    public CustomException(HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }
}
