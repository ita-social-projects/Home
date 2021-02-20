package com.softserveinc.ita.homeproject.application.exception.mapper.jax;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.exception.mapper.BaseExceptionMapper;

@Provider
public class NotFoundExceptionMapper extends BaseExceptionMapper<NotFoundException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_FOUND;
    }

    @Override
    protected String extractMessage(NotFoundException exception) {
        return exception.getMessage();
    }
}
