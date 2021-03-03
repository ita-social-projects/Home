package com.softserveinc.ita.homeproject.application.exception.mapper;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionMapper extends BaseExceptionMapper<Throwable> {

    protected Response.Status getStatus() {
        return INTERNAL_SERVER_ERROR;
    }

    protected String extractMessage(Throwable exception) {
        return "Unknown error. Please contact support.";
    }
}
