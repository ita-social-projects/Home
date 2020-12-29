package com.softserveinc.ita.homeproject.homeservice.exception;

public abstract class BaseHomeException extends RuntimeException {

    public BaseHomeException(String message) {
        super(message);
    }
}
