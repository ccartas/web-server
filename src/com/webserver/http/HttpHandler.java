package com.webserver.http;

import com.webserver.exceptions.HttpHeaderFormatException;
import com.webserver.exceptions.InvalidProtocolException;
import com.webserver.exceptions.InvalidRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Cosmin on 8/29/2016.
 */

/**
 * # Interface used for defining HTTP operations
 * */
public interface HttpHandler {

    public HttpRequest processHttpRequest(InputStream input) throws InvalidRequestException, InvalidProtocolException, IOException, HttpHeaderFormatException;
    public HttpResponse handleRequest(HttpRequest request) throws IOException;
    public HttpResponse handleGETRequest(HttpRequest request) throws IOException;
    public HttpResponse handlePOSTRequest(HttpRequest request) throws IOException;
    public HttpResponse handleDELETERequest(HttpRequest request) throws IOException;
    public HttpResponse handlePUTRequest(HttpRequest request) throws IOException;
    public void sendResponse(HttpResponse response, OutputStream output) throws IOException;
    public boolean checkKeepAlive(HttpRequest request, HttpResponse response);
}
