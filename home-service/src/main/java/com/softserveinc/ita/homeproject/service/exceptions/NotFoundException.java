package com.softserveinc.ita.homeproject.service.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException{

    public NotFoundException(HttpStatus status, String msg) {
        super(status, msg);
    }
}
