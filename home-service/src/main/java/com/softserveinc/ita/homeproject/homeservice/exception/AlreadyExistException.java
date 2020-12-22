package com.softserveinc.ita.homeproject.homeservice.exception;

/**
 * AlreadyExistException is the class that
 * is used to throw exception when there's
 * already existing element in the database.
 *
 * @author Mykyta Morar
 */
public class AlreadyExistException extends HomeException {

    public AlreadyExistException(String message) {
        super(message);
    }
}
