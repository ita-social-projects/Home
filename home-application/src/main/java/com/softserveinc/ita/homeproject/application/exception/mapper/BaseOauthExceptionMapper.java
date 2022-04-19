package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.application.security.exception.BaseOauthException;

public class BaseOauthExceptionMapper<T extends BaseOauthException> extends BaseExceptionMapper<T> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }

    @Override
    protected String extractMessage(T exception) {
        return exception.getMessage();
    }
}
