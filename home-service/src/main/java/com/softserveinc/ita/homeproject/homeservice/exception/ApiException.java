package com.softserveinc.ita.homeproject.homeservice.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
