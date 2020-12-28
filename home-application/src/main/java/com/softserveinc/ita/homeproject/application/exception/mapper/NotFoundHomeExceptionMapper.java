package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;

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
public class NotFoundHomeExceptionMapper extends BaseHomeExceptionMapper<NotFoundHomeException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.NOT_FOUND;
    }
}
