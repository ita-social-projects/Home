package com.softserveinc.ita.homeproject.homeservice.exception;

/**
 * NotFoundException is the class that
 * is used to throw exception when there's
 * no searched element in the database.
 *
 * @author Mykyta Morar
 */
public class NotFoundException extends HomeException {

    public NotFoundException(String message) {
        super(message);
    }
}
