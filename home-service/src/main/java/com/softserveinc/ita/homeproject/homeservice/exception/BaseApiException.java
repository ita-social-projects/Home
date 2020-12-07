package com.softserveinc.ita.homeproject.homeservice.exception;

public abstract class BaseApiException extends RuntimeException {

    public BaseApiException(String message) {
        super(message);
    }
}
