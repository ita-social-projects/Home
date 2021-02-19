package com.softserveinc.ita.homeproject.application.exception.mapper;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static com.softserveinc.ita.homeproject.application.exception.mapper.ExceptionMapperUtils.getInvalidDataApiExeptionParser;

@Provider
public class InvalidDataAccessApiExceptionMapper extends BaseExceptionMapper<InvalidDataAccessApiUsageException> {
    @Override
    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String extractMessage(InvalidDataAccessApiUsageException exception) {

        return getInvalidDataApiExeptionParser(exception);
    }
}
