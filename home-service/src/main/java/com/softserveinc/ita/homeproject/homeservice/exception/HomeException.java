package com.softserveinc.ita.homeproject.homeservice.exception;

/**
 * HomeException is the base class for all custom
 * exceptions that needs to be thrown in the application.
 *
 * @author Mykyta Morar
 */
public class HomeException extends RuntimeException {

    public HomeException(String message) {
        super(message);
    }
}
