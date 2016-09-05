package com.webserver.http;

import com.webserver.utils.ContentType;
import com.webserver.utils.HttpHeaders;
import com.webserver.utils.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Cosmin on 8/25/2016.
 */

/**
 * HttpResponse class is used to map the HTTP response that is sent to the client;
 * Fileds:
 * protocol - mapping the HTTP protocol used;
 * httpStatus - mapping the Status Code returned to the client;
 * headers - mapping the HTTP response headers;
 * responseBody - mapping the HTTP response body that is sent to the client;
 */
public class HttpResponse {

    private String protocol;
    private HttpStatus httpStatus;
    private Map<String, String> headers;
    private byte[] responseBody;

    public HttpResponse(){
        this.headers = new HashMap<String, String>();
    }

    public void setProtocol(String protocol){
        this.protocol = protocol;
    }
    public void setHttpStatus(HttpStatus status){
        this.httpStatus = status;
    }
    public void setDateHeader(Date date){
        headers.put(HttpHeaders.DATE.getHttpHeader(), date.toString());
    }
    public void setContentLengthHeader(int value){
        headers.put(HttpHeaders.CONTENT_LENGTH.getHttpHeader(), String.valueOf(value));
    }
    public void setContentTypeHeader(ContentType type){
        headers.put(HttpHeaders.CONTENT_TYPE.getHttpHeader(), type.getContentType());
    }
    public void setResponseBody(byte[] body){
        this.responseBody = body;
    }


    public String getProtocol(){
        return this.protocol;
    }
    public byte[] getResponseBody(){
        return responseBody;
    }
    public Map<String, String> getResponseHeaders(){
        return this.headers;
    }


    @Override
    public String toString() {
        String response = protocol + " " + this.httpStatus.getHttpStatus() + "\n";
        response += this.headers.entrySet().stream()
                .map(entry -> entry.getKey() + ": "+entry.getValue())
                .collect(Collectors.joining("\n"));

        response += "\n"+"\r\n";
        return response;
    }
}
