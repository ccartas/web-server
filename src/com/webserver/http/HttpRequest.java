package com.webserver.http;

import com.webserver.utils.HttpMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Cosmin on 8/25/2016.
 */

/**
 * HttpRequest class is used to map the HTTP requests coming from the client's browser;
 * Used fields:
 * methodType - mapping the type of HTTP method that is performed
 * requestURI - mapping the HTTP request path
 * requestHeaders - mapping the request headers (NavigableMap interface is used in order to preserve the insertion order)
 * requestBody - mapping the requestBody
 * */

public class HttpRequest {

    private HttpMethod methodType;
    private String requestURI;
    private String protocol;
    private NavigableMap<String,String> requestHeaders;
    private byte[] requestBody;

    public HttpRequest(){
        this.requestHeaders = new TreeMap<>();
    }

    public String getRequestURI(){
        return this.requestURI;
    }
    public HttpMethod getHttpMethod(){
        return this.methodType;
    }
    public String getProtocol(){
        return this.protocol;
    }
    public NavigableMap<String, String> getRequestHeaders(){
        return this.requestHeaders;
    }
    public byte[] getRequestBody(){
        return this.requestBody;
    }


    public void setMethodType(HttpMethod methodType){
        this.methodType = methodType;
    }
    public void setRequestURI(String requestURI){
        this.requestURI = requestURI;
    }
    public void setProtocol(String protocol){
        this.protocol = protocol;
    }
    public void setRequestBody(byte[] body){
        this.requestBody = body;
    }

    @Override
    public String toString(){
        String request = this.methodType.getHttpMethod() + " " + this.requestURI + " " + this.protocol + "\n";
        request += this.requestHeaders.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\n"));

        return request;
    }


}
