package com.softserveinc.ita.homeproject.service.exceptions;

public class NotFoundException extends ApiException{
    public NotFoundException(String message) {
        super(message);
    }
}
