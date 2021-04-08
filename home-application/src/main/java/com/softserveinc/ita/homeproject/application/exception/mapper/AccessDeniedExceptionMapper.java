package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.security.access.AccessDeniedException;

/**
 * AccessDeniedExceptionMapper class is used to return exception
 * response with HTTP status code, when the authorization is failed.
 *
 * @author Oleksii Zinkevych
 * @see javax.ws.rs.ext.ExceptionMapper
 */
@Provider
public class AccessDeniedExceptionMapper extends BaseExceptionMapper<AccessDeniedException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.UNAUTHORIZED;
    }

    @Override
    protected String extractMessage(AccessDeniedException exception) {
        return "Unauthorized";
    }
}
