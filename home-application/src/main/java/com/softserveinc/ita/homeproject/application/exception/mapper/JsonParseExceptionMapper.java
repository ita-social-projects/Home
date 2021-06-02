package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * JsonParseExceptionMapper class is used to return exception
 * response with HTTP status code, when json parsing exception occurs.
 *
 * @author Oleksii Zinkevych
 * @see javax.ws.rs.ext.ExceptionMapper
 */
@Provider
public class JsonParseExceptionMapper extends BaseExceptionMapper<JsonParseException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String extractMessage(JsonParseException exception) {
        return "Bad json";
    }
}
