package com.webserver.utils;


/**
 * Created by Cosmin on 8/25/2016.
 */
public enum HttpStatus {
    OK("200 OK"),
    INTERNAL_SERVER_ERROR("500 Internal Server Error"),
    BAD_REQUEST("400 Bad Request"),
    FORBIDDEN("403 Forbidden"),
    NOT_FOUND("404 Not Found");

    private String httpStatus;

    private HttpStatus(String httpStatus){
        this.httpStatus = httpStatus;
    }

    public String getHttpStatus(){
        return this.httpStatus;
    }

}
