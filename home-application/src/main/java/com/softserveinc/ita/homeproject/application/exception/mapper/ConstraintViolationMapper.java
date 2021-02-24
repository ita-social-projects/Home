package com.softserveinc.ita.homeproject.application.exception.mapper;

import com.softserveinc.ita.homeproject.homeservice.exception.BaseHomeException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;


@Provider
public class ConstraintViolationMapper extends BaseExceptionMapper<ConstraintViolationException>{

    @Override
    protected Response.Status getStatus(){ return Response.Status.BAD_REQUEST;}

    @Override
    protected String extractMessage(ConstraintViolationException exception) {
        return handleConstraintViolationExceptionMessage(exception);
    }

    private String handleConstraintViolationExceptionMessage(ConstraintViolationException exception) {

        StringBuilder result = new StringBuilder();
        Map<String, String> parameters = new HashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            Path violationPath = violation.getPropertyPath();
            String parameter = "";
            for (Path.Node node : violationPath) {
                parameter = node.getName();
            }
            parameters.put(parameter,violation.getMessage());
        }

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            result.append("Parameter ").append(entry.getKey()).append(" is invalid - ");
            result.append(entry.getValue()).append(". ");
        }
        return result.toString().trim();
    }


}
