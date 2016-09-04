package com.webserver.utils;

/**
 * Created by Cosmin on 9/4/2016.
 */
public enum HtmlErrorMessages {

    FILE_NOT_FOUND("<html><body><h1>404 File Not Found.</h1></body></html>"),
    INTERNAL_SERVER_ERROR("<html><body><h1>500 Internal Server Error.</h1></body></html>"),
    ACCESS_FORBIDDEN("<html><body><h1>403 Access Forbidden.</h1></body></html>"),
    BAD_REQUEST("<html><body<h1>400 Bad Request.</h1></body></html>");


    private String errorMessage;
    private HtmlErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
}
