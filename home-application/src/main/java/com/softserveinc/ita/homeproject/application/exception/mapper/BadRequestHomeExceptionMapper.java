package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;

import javax.ws.rs.core.Response;

/**
 * BadRequestHomeExceptionMapper class is used to return exception
 * response with HTTP status code, when a request not match API requirements.
 *
 * @author Oleksii Zinkevych
 * @see javax.ws.rs.ext.ExceptionMapper
 */
public class BadRequestHomeExceptionMapper extends BaseHomeExceptionMapper<BadRequestHomeException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }
}
