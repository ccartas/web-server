package com.webserver.exceptions;

/**
 * Created by Cosmin on 9/1/2016.
 */
public class InvalidRequestException extends Exception {

    public InvalidRequestException(String errorMessage){
        super(errorMessage);
    }
}
