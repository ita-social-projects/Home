package com.softserveinc.ita.homeproject.service.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception{

    private HttpStatus status;

    public ApiException (HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }
}
