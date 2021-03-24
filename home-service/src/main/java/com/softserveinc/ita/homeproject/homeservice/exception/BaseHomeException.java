package com.softserveinc.ita.homeproject.homeservice.exception;

public abstract class BaseHomeException extends RuntimeException {

    protected BaseHomeException(String message) {
        super(message);
    }
}
