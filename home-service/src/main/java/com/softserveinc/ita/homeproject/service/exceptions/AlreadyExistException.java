package com.softserveinc.ita.homeproject.service.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends ApiException{

    public AlreadyExistException(HttpStatus status, String msg) {
        super(status, msg);
    }
}
