package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.application.security.exception.BaseOauthException;

public abstract class BaseOauthExceptionMapper<T extends BaseOauthException> extends BaseExceptionMapper<T> {

    @Override
    protected String extractMessage(T exception) {
        return exception.getMessage();
    }
}
