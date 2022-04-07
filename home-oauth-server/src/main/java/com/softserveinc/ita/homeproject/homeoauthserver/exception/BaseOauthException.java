package com.softserveinc.ita.homeproject.homeoauthserver.exception;

public class BaseOauthException extends RuntimeException {

    protected BaseOauthException(String message) {
        super(message);
    }
}
