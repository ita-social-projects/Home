package com.softserveinc.ita.homeproject.homeservice.exception;

public class NotFoundException extends ApiException{
    public NotFoundException(String message) {
        super(message);
    }
}
