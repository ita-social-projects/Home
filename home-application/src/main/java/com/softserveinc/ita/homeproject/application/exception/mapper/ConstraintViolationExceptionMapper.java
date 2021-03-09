package com.softserveinc.ita.homeproject.application.exception.mapper;

import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


@Provider
public class ConstraintViolationExceptionMapper extends BaseExceptionMapper<ConstraintViolationException> {

    @Override
    protected Response.Status getStatus(){
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String extractMessage(ConstraintViolationException exception) {
        return handleConstraintViolationExceptionMessage(exception);
    }

    private String handleConstraintViolationExceptionMessage(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
            .map(violation -> String.format("Parameter `%s` is invalid - %s.",
                getParameterFromPropertyPath(violation.getPropertyPath()),
                violation.getMessage())
            )
            .collect(Collectors.joining(" "));
    }

    private String getParameterFromPropertyPath(Path path){
        String result = "";
        for(Path.Node node: path){
            result = node.getName();
        }
        return result;
    }
}

