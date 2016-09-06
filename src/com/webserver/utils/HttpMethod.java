package com.webserver.utils;

/**
 * Created by Cosmin on 8/26/2016.
 */
public enum HttpMethod {

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String method;

    private HttpMethod(String method){
        this.method = method;
    }

    public String getHttpMethod(){
        return this.method;
    }
}
