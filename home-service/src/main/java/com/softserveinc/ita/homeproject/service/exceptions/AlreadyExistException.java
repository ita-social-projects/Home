package com.softserveinc.ita.homeproject.service.exceptions;

public class AlreadyExistException extends Exception{
    private int code;
    public AlreadyExistException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
