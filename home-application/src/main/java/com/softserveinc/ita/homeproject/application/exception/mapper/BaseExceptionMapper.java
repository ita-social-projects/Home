package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseHomeException;
import com.softserveinc.ita.homeproject.model.ApiError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Abstract class for handling the mapping to the response
 * Changeable data is in abstract method.
 *
 * @param <T> type of incoming exception
 *
 * @author Mykyta Morar
 */
public abstract class BaseExceptionMapper<T extends Throwable>  implements ExceptionMapper<T> {

    @Override
    public Response toResponse(T exception) {
     Response.Status status = getStatus();
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ApiError()
                        .responseCode(status.getStatusCode())
                        .errorMessage(extractMessage(exception)))
                        .build();
    }
    protected abstract Response.Status getStatus();

    protected abstract String extractMessage(T exception);

}


