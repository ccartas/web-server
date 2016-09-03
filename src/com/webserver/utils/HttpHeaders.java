package com.webserver.utils;

/**
 * Created by Cosmin on 8/29/2016.
 */

public enum HttpHeaders {

    HOST("Host"),
    CONNECTION("Connection"),
    USER_AGENT("User-Agent"),
    ACCEPT("Accept"),
    DATE("Date"),
    REFERER("Referer"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type");

    private String httpHeader;

    private HttpHeaders(String header){
        this.httpHeader = header;
    }
    public String getHttpHeader(){
        return this.httpHeader;
    }
}
