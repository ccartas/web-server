package com.webserver.exceptions;

/**
 * Created by Cosmin on 8/29/2016.
 */
public class BadRequestException extends Exception {

    public BadRequestException(String exceptionMessage){
        super(exceptionMessage);
    }
}
