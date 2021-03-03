package com.softserveinc.ita.homeproject.application.exception.mapper;

import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseHomeException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;


@Provider
public class ConstraintViolationMapper extends BaseExceptionMapper<ConstraintViolationException> {

    @Override
    protected Response.Status getStatus(){
        return Response.Status.BAD_REQUEST;
    }

    @Override
    protected String extractMessage(ConstraintViolationException exception) {
        return handleConstraintViolationExceptionMessage(exception);
    }

    private String handleConstraintViolationExceptionMessage(ConstraintViolationException exception) {
        StringBuilder result = new StringBuilder();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            result.append("Parameter ")
                .append(getParameterFromPropertyPath(violation.getPropertyPath()))
                .append(" is invalid - ")
                .append(violation.getMessage())
                .append(". ");
        }
        return result.toString().trim();
    }

    private String getParameterFromPropertyPath(Path path){
        String result = "";
        for(Path.Node node: path){
            result = node.getName();
        }
        return result;
    }
}
