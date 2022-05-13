package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.security.exception.NotAcceptableOauthException;

@Provider
public class NotAcceptableOauthExceptionMapper extends BaseOauthExceptionMapper<NotAcceptableOauthException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }
}
