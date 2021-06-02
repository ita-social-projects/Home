package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * JsonMappingExceptionMapper class is used to return exception
 * response with HTTP status code, when json mapping exception occurs.
 *
 * @author Oleksii Zinkevych
 * @see javax.ws.rs.ext.ExceptionMapper
 */
@Provider
public class JsonMappingExceptionMapper extends BaseExceptionMapper<JsonMappingException> {

    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String extractMessage(JsonMappingException exception) {
        String exceptionMessage = exception.getMessage();
        if (exceptionMessage.contains("problem: ")) {
            return exceptionMessage
                .substring(exceptionMessage.indexOf("problem: ") + 9, exceptionMessage.indexOf("\n"));
        } else if (exceptionMessage.contains("Cannot deserialize value of type `java.time.LocalDateTime`")) {
            return "Bad date-time format: '" + exceptionMessage
                .substring(exceptionMessage.indexOf("from String ") + 13,
                    exceptionMessage.indexOf("Failed to deserialize") - 3) + "'";
        } else {
            return "Bad json";
        }
    }
}
