package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.PasswordException;


@Provider
public class PasswordExceptionMapper extends BaseHomeExceptionMapper<PasswordException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_ACCEPTABLE;
    }
}
