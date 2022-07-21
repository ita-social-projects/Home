package com.softserveinc.ita.homeproject.homeoauthserver.exception.handler;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.UNKNOWN_ERROR_APPEARED_MESSAGE;

import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotAcceptableOauthException;
import com.softserveinc.ita.homeproject.homeoauthserver.exception.NotFoundOauthException;
import com.softserveinc.ita.homeproject.homeoauthserver.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundOauthException.class})
    protected ResponseEntity<ApiError> handleError404(NotFoundOauthException ex) {
        ApiError apiError = new ApiError();
        apiError.setResponseCode(404);
        apiError.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotAcceptableOauthException.class})
    protected ResponseEntity<ApiError> handleError406(NotAcceptableOauthException ex) {
        ApiError apiError = new ApiError();
        apiError.setResponseCode(406);
        apiError.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<ApiError> handleError400() {
        ApiError apiError = new ApiError();
        apiError.setResponseCode(400);
        apiError.setErrorMessage(UNKNOWN_ERROR_APPEARED_MESSAGE);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}

