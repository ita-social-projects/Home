package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseApiException;

import javax.ws.rs.core.Response;

/**
 * Abstract class for handling the mapping to the response
 * Changeable data is in abstract method.
 *
 * @param <T> type of incoming exception
 *
 * @author Mykyta Morar
 */
public abstract class BaseApiExceptionMapper<T extends BaseApiException> {
    public abstract Response toResponse(T exception, Response.Status responseStatus);
}
