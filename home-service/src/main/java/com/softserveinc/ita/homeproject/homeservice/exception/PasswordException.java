package com.softserveinc.ita.homeproject.homeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class PasswordException extends BaseHomeException {
    public PasswordException(String message) {
        super(message);
    }
}
