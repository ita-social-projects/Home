package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseApiException;
import com.softserveinc.ita.homeproject.model.ApiError;

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
public abstract class BaseApiExceptionMapper<T extends BaseApiException>  implements ExceptionMapper<T> {
    @Override
    public Response toResponse(T exception) {
        return Response.status(getStatus())
                .entity(new ApiError()
                        .responseCode(getStatus().getStatusCode())
                        .errorMessage(exception.getMessage()))
                .build();
    }

    protected abstract Response.Status getStatus();

}
