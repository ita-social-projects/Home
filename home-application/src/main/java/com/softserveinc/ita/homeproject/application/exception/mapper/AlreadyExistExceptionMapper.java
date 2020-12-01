package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistException;
import com.softserveinc.ita.homeproject.model.ApiError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyExistExceptionMapper implements ExceptionMapper<AlreadyExistException> {
    @Override
    public Response toResponse(AlreadyExistException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ApiError()
                        .responseCode(Response.Status.CONFLICT.getStatusCode())
                        .errorMessage(exception.getMessage()))
                .build();
    }
}
