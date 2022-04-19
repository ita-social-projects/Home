package com.softserveinc.ita.homeproject.application.security.exception;

public abstract class BaseOauthException extends RuntimeException {

    protected BaseOauthException(String message) {
        super(message);
    }
}
