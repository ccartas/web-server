package com.webserver.utils;

/**
 * Created by Cosmin on 8/26/2016.
 */

/**
 * Enum with constants for the Content-Type HTTP Header
 */
public enum ContentType {
    TEXT("text/plain"),
    HTML("text/html"),
    CSS("text/css"),
    JS("application/js"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    PNG("image/png");

    private String contentType;

    private ContentType(String contentType){
        this.contentType = contentType;
    }

    public String getContentType(){
        return this.contentType;
    }


}
