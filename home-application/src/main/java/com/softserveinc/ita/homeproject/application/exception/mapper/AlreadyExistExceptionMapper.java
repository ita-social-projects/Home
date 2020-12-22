package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistApiException;
import com.softserveinc.ita.homeproject.model.ApiError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * AlreadyExistExceptionMapper class is used to return exception
 * response with HTTP status code, when the element already exists.
 *
 * @see javax.ws.rs.ext.ExceptionMapper
 *
 * @author Ihor Svyrydenko
 */
@Provider
public class AlreadyExistExceptionMapper extends BaseApiExceptionMapper<AlreadyExistApiException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }
}
