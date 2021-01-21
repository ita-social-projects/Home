package com.softserveinc.ita.homeproject.application.exception.mapper;

import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper extends BaseExceptionMapper<AccessDeniedException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.FORBIDDEN;
    }

    @Override
    protected String extractMessage(AccessDeniedException exception) {
        return "You don't have permission.";
    }
}
