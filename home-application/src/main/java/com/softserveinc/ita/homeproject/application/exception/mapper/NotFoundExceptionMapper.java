package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundApiException;
import com.softserveinc.ita.homeproject.model.ApiError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * NotFoundExceptionMapper class is used to return exception
 * response with HTTP status code, when the element is not found.
 *
 * @see javax.ws.rs.ext.ExceptionMapper
 *
 * @author Ihor Svyrydenko
 */
@Provider
public class NotFoundExceptionMapper extends BaseApiExceptionMapper<NotFoundApiException> {
    @Override
    public Response toResponse(NotFoundApiException exception, Response.Status responseStatus) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiError()
                        .responseCode(Response.Status.NOT_FOUND.getStatusCode())
                        .errorMessage(exception.getMessage()))
                .build();
    }
}
