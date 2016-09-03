package com.webserver.exceptions;

/**
 * Created by Cosmin on 9/1/2016.
 */
public class HttpHeaderFormatException extends Exception {

    public HttpHeaderFormatException(String errorMessage){
        super(errorMessage);
    }
}
