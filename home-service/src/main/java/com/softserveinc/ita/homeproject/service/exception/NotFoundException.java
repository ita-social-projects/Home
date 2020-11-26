package com.softserveinc.ita.homeproject.service.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

    public NotFoundException(HttpStatus status, String msg) {
        super(status, msg);
    }
}
