package com.softserveinc.ita.homeproject.homeservice.exceptions;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
