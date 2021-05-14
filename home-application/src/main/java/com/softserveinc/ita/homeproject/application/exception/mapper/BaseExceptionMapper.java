package com.softserveinc.ita.homeproject.application.exception.mapper;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Providers;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseHomeException;
import com.softserveinc.ita.homeproject.model.ApiError;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class for handling the mapping to the response
 * Changeable data is in abstract method.
 *
 * @param <T> type of incoming exception
 * @author Mykyta Morar
 */
@Slf4j
public abstract class BaseExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {
    @Context
    private Providers providers;

    @Override
    public Response toResponse(T exception) {
        log.info("Mapped error.", exception);
        var throwable = exception.getCause();

        if(throwable instanceof BaseHomeException) {
            ExceptionMapper mapper = providers.getExceptionMapper(throwable.getClass());
            if (mapper != null) {
                return mapper.toResponse(throwable);
            }
        }
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


