package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;

/**
 * NotFoundExceptionMapper class is used to return exception
 * response with HTTP status code, when the element is not found.
 *
 * @author Ihor Svyrydenko
 * @see javax.ws.rs.ext.ExceptionMapper
 */
@Provider
public class NotFoundHomeExceptionMapper extends BaseHomeExceptionMapper<NotFoundHomeException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_FOUND;
    }
}
