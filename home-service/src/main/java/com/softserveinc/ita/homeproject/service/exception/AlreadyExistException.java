package com.softserveinc.ita.homeproject.service.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends CustomException {

    public AlreadyExistException(HttpStatus status, String msg) {
        super(status, msg);
    }
}
