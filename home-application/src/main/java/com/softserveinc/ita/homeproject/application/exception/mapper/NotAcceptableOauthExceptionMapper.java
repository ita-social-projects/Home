package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.application.security.exception.NotAcceptableOauthException;

public class NotAcceptableOauthExceptionMapper extends BaseOauthExceptionMapper<NotAcceptableOauthException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }
}
