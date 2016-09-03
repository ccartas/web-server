package com.webserver.http;

import com.webserver.exceptions.HttpHeaderFormatException;
import com.webserver.exceptions.InvalidProtocolException;
import com.webserver.exceptions.InvalidRequestException;
import com.webserver.handlers.FileHandler;
import com.webserver.server.Server;
import com.webserver.utils.ContentType;
import com.webserver.utils.HttpMethod;
import com.webserver.utils.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;

/**
 * Created by Cosmin on 8/25/2016.
 */
public class HttpTask implements Runnable, HttpHandler {


    private final Server server;
    private final Socket client;

    public HttpTask(Socket client, Server server){
        this.client = client;
        this.server = server;
    }


    @Override
    public void run() {
        try{

            HttpRequest request = processHttpRequest(client.getInputStream());
            HttpResponse response = handleRequest(request);

            if(checkKeepAlive(request,response)) {
                sendResponse(response, client.getOutputStream());
                server.submitRequest(client);
            }
            else
            {
                response.getResponseHeaders().put("Connection","close");
                sendResponse(response, client.getOutputStream());
                client.close();
            }
        }
        catch(InvalidRequestException ex){
            System.err.println(ex.getMessage());
        }
        catch(InvalidProtocolException ex){
            System.err.println(ex.getMessage());
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(HttpHeaderFormatException ex){
            System.err.println(ex.getMessage());
        }

    }

    /**
     * Method that process all the data from the InputStream parameter and transforms it into
     * a HttpRequest Object
     * @param input - InputStream from the Socket;
     * @return HttpRequest - the processed request;
     * @throws InvalidRequestException - Exception Thrown if the first line of the Request is not valid;
     * @throws InvalidProtocolException - Exception Thrown if the protocol used in the Request is not HTTP
     * @throws IOException - Exception Thrown if the requested file is not found;
     * @throws HttpHeaderFormatException - Exception Thrown if the HTTP Request Header has other format than (Header_Name: Header_Value);
     */
    @Override
    public HttpRequest processHttpRequest(InputStream input) throws InvalidRequestException, InvalidProtocolException, IOException, HttpHeaderFormatException {
        HttpRequest request = new HttpRequest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        /**
        * Basic checks for HTTP request (e.g: GET /index.html HTTP/1.1)
        * */
        String requestLine = reader.readLine();
        if(requestLine == null){
            throw new InvalidRequestException("Not a valid HTTP request.");
        }
        String[] values = requestLine.split(" ");
        if(values.length != 3){
            throw new InvalidRequestException("Invalid request.");
        }
        if(!values[2].startsWith("HTTP/")){
            throw new InvalidProtocolException("Server accepts HTTP requests only.");
        }
        request.setMethodType(HttpMethod.valueOf(values[0]));
        request.setRequestURI(values[1]);
        request.setProtocol(values[2]);

        /**
        * Parsing HTTP request headers and save it in a key, value collection
        */
        requestLine = reader.readLine();
        while(requestLine != null && !requestLine.equals("")){
            String[] header = requestLine.split(": ");
            if(header.length != 2){
                throw new HttpHeaderFormatException("Invalid HTTP header: " + requestLine);
            }
            else
            {
                request.getRequestHeaders().put(header[0], header[1]);
            }
            requestLine=reader.readLine();
        }


        if (request.getHttpMethod().equals("POST")){

        }
        return request;
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse();
        if(request.getHttpMethod().equals(HttpMethod.GET)) {
            return handleGETRequest(request);
        }
        return null;

        }

    @Override
    public HttpResponse handleGETRequest(HttpRequest request) throws IOException {
        File webApp = new File("webapps" + request.getRequestURI());
        HttpResponse response = new HttpResponse();
        response.setProtocol(request.getProtocol());
            if(webApp.exists()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setDateHeader(new Date());
                InputStream fis = new FileInputStream(webApp);
                response.setContentLengthHeader(fis.available());
                byte[] body = new byte[fis.available()];
                fis.read(body);
                fis.close();
                response.setResponseBody(body);
                response.setContentTypeHeader(FileHandler.getContentTypeFromFileExtension(webApp));
            }
            else
            {
                String errorMessage = "<html><body><h1>404 File not found.</h1></body></html>";
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                response.setDateHeader(new Date());
                response.setContentTypeHeader(ContentType.HTML);
                response.setContentLengthHeader(errorMessage.length());
                response.setResponseBody(errorMessage.getBytes());
            }
        return response;
    }

    @Override
    public HttpResponse handlePOSTRequest(HttpRequest request) throws IOException {
        return null;
    }

    @Override
    public HttpResponse handleDELETERequest(HttpRequest request) throws IOException {
        return null;
    }

    @Override
    public HttpResponse handlePUTRequest(HttpRequest request) throws IOException {
        return null;
    }


    @Override
    public void sendResponse(HttpResponse response, OutputStream output) throws IOException {
        System.out.println(response.toString());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        writer.write(response.toString());
        writer.flush();

        if(response.getResponseBody()!=null && response.getResponseBody().length>0){
            output.write(response.getResponseBody());
        }
        output.flush();

    }

    @Override
    public boolean checkKeepAlive(HttpRequest request, HttpResponse response){
        if(request.getRequestHeaders().containsKey("Connection")  && request.getRequestHeaders().get("Connection").equalsIgnoreCase("close")){
            return false;
        }
        if(request.getProtocol().equals("HTTP/1.1")){
            if(request.getRequestHeaders().containsKey("Connection")  && request.getRequestHeaders().get("Connection").equalsIgnoreCase("close")){
                return false;
            }
            else
            {
                return true;
            }
        }
        return false;
    }
}
