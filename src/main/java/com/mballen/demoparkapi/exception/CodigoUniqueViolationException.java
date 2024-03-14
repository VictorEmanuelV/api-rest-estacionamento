package com.mballen.demoparkapi.exception;

public class CodigoUniqueViolationException extends RuntimeException {

    public CodigoUniqueViolationException(String message){
        super(message);
    }

}
