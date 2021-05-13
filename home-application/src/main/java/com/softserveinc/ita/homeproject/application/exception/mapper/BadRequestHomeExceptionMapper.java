package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;

/**
 * BadRequestHomeExceptionMapper class is used to return exception
 * response with HTTP status code, when a request not match API requirements.
 *
 * @author Oleksii Zinkevych
 * @see javax.ws.rs.ext.ExceptionMapper
 */

@Provider
public class BadRequestHomeExceptionMapper extends BaseHomeExceptionMapper<BadRequestHomeException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }
}
