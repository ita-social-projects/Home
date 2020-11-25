package com.softserveinc.ita.homeproject.homeservice.exceptions;

public class NotFoundException extends ApiException{
    public NotFoundException(String message) {
        super(message);
    }
}
