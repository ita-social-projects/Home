package com.softserveinc.ita.homeproject.application.exception.mapper;

import static com.softserveinc.ita.homeproject.application.exception.mapper.ExceptionMapperUtils.getInvalidDataApiExceptionParser;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.dao.InvalidDataAccessApiUsageException;

@Provider
public class InvalidDataAccessApiExceptionMapper extends BaseExceptionMapper<InvalidDataAccessApiUsageException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String extractMessage(InvalidDataAccessApiUsageException exception) {

        return getInvalidDataApiExceptionParser(exception);
    }
}
