package com.softserveinc.ita.homeproject.application.exception.mapper;

import static org.postgresql.util.PSQLState.NOT_NULL_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.model.ApiError;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;

@Provider
public class DataIntegrityViolationExceptionMapper implements ExceptionMapper<DataIntegrityViolationException> {

    public final static String SERVER_ERROR_MESSAGE = "Unknown error. Please contact support.";

    @Override
    public Response toResponse(DataIntegrityViolationException exception) {
        return handleDataIntegrityViolationExceptionMessage(exception);
    }

    private Response handleDataIntegrityViolationExceptionMessage(DataIntegrityViolationException exception) {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String message = SERVER_ERROR_MESSAGE;

        Throwable rootCause = exception.getMostSpecificCause();
        PSQLException psqlException = ((PSQLException) rootCause);
        String sqlState = psqlException.getSQLState();

        if (UNIQUE_VIOLATION.getState().equals(sqlState)) {
            status = Response.Status.BAD_REQUEST;
            message = String.format("The %s must be unique. %s",
                    psqlException.getServerErrorMessage().getTable(),
                    psqlException.getServerErrorMessage().getDetail().replaceAll("[()]", "`"));
        } else if (NOT_NULL_VIOLATION.getState().equals(sqlState)) {
            status = Response.Status.BAD_REQUEST;
            message = String.format("Parameter `%s` is invalid - must not be null.",
                    psqlException.getServerErrorMessage().getColumn());
        }
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ApiError()
                        .responseCode(status.getStatusCode())
                        .errorMessage(message))
                .build();
    }
}
