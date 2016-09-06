package com.webserver.http;

import com.webserver.exceptions.HttpHeaderFormatException;
import com.webserver.exceptions.InvalidProtocolException;
import com.webserver.exceptions.InvalidRequestException;
import com.webserver.handlers.FileHandler;
import com.webserver.server.Server;
import com.webserver.utils.ContentType;
import com.webserver.utils.HtmlMessages;
import com.webserver.utils.HttpMethod;
import com.webserver.utils.HttpStatus;

import java.io.*;
import java.net.Socket;
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
            try {
                sendResponse(createSimpleResponse("HTTP/1.1", HttpStatus.BAD_REQUEST, HtmlMessages.BAD_REQUEST), client.getOutputStream());
            }
            catch(IOException e){

            }
        }
        catch(InvalidProtocolException ex){
            System.err.println(ex.getMessage());
            try {
                sendResponse(createSimpleResponse("HTTP/1.0", HttpStatus.BAD_REQUEST, HtmlMessages.BAD_REQUEST), client.getOutputStream());
            }
            catch(IOException e){

            }
        }
        catch(IllegalArgumentException ex){
            try {
                sendResponse(createSimpleResponse("HTTP/1.1", HttpStatus.BAD_REQUEST, HtmlMessages.BAD_REQUEST), client.getOutputStream());
            }
            catch(IOException e){

            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch(HttpHeaderFormatException ex){
            System.err.println(ex.getMessage());
            try {
                sendResponse(createSimpleResponse("HTTP/1.1", HttpStatus.BAD_REQUEST, HtmlMessages.BAD_REQUEST), client.getOutputStream());
            }
            catch(IOException e){

            }
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
    public HttpRequest processHttpRequest(InputStream input) throws InvalidRequestException, InvalidProtocolException, IOException, HttpHeaderFormatException, IllegalArgumentException {
        HttpRequest request = new HttpRequest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        /**
        * Basic checks for HTTP request (e.g: GET /index.html HTTP/1.1)
        * */

            String requestLine = reader.readLine();

            if (requestLine == null) {
                throw new InvalidRequestException("Not a valid HTTP request.");
            }
            String[] values = requestLine.split(" ");
            if (values.length != 3) {
                throw new InvalidRequestException("Invalid request.");
            }
            if (!values[2].startsWith("HTTP/")) {
                throw new InvalidProtocolException("Server accepts HTTP requests only.");
            }
            request.setMethodType(HttpMethod.valueOf(values[0]));
            request.setRequestURI(values[1]);
            request.setProtocol(values[2]);

            /**
             * Parsing HTTP request headers and save it in a key, value collection
             */
            while (((requestLine = reader.readLine()) != null) && !requestLine.equals("")) {
                String[] header = requestLine.split(": ");
                if (header.length != 2) {
                    throw new HttpHeaderFormatException("Invalid HTTP header: " + requestLine);
                } else {
                    request.getRequestHeaders().put(header[0], header[1]);
                }
            }

        return request;
    }

    /**
     * Method that performs absolute file traversal exploit (if the client tries to access a file outside the webapps folder)
     * @param request - incoming request from the client;
     * @param requestedFile - the file that client requested;
     * @return - If the client requests a forbidden file it will return a FORBIDDEN HTTP response. If the request is fine the method will return null;
     */
    public HttpResponse checkForAbsolutePathTraversal(HttpRequest request, File requestedFile){
        File rootDir = new File("webapps");
        try {
            if (!requestedFile.getCanonicalPath().startsWith(rootDir.getCanonicalPath())) {
                System.out.println(requestedFile.getCanonicalPath());
                return createSimpleResponse(request.getProtocol(), HttpStatus.FORBIDDEN, HtmlMessages.ACCESS_FORBIDDEN);
            }
        }
        catch(IOException ex){
            return createSimpleResponse(request.getProtocol(), HttpStatus.INTERNAL_SERVER_ERROR, HtmlMessages.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    /**
     * Method that will return a specific HTTP response based on a specific request;
     * @param request - incoming request from the clinet;
     * @return - It will give a specific response
     */
    @Override
    public HttpResponse handleRequest(HttpRequest request) {
        try {
            if (request.getHttpMethod().equals(HttpMethod.GET)) {
                return handleGETRequest(request);
            } else if (request.getHttpMethod().equals(HttpMethod.HEAD)) {
                return handleHEADRequest(request);
            } else if (request.getHttpMethod().equals(HttpMethod.PUT)) {
                return handlePUTRequest(request);
            } else if (request.getHttpMethod().equals(HttpMethod.DELETE)) {
                return handleDELETERequest(request);
            }
        }
        catch(IllegalArgumentException ex){
            ex.printStackTrace();
            return createSimpleResponse("HTTP/1.1", HttpStatus.BAD_REQUEST, HtmlMessages.BAD_REQUEST);

        }
        return createSimpleResponse("HTTP/1.1", HttpStatus.BAD_REQUEST, HtmlMessages.BAD_REQUEST);
        }

    /**
     * Method that will process GET request and give a specific response
     * @param request
     * @return
     */
    @Override
    public HttpResponse handleGETRequest(HttpRequest request){
        File getFile = new File("webapps" + request.getRequestURI());
        HttpResponse response = new HttpResponse();
        response.setProtocol(request.getProtocol());

        HttpResponse badResponse = checkForAbsolutePathTraversal(request, getFile);
        if(badResponse!=null){
            return badResponse;
        }
            if (getFile.exists()) {
                try {
                    response.setHttpStatus(HttpStatus.OK);
                    response.setDateHeader(new Date());
                    InputStream fis = new FileInputStream(getFile);
                    response.setContentLengthHeader(fis.available());
                    byte[] body = new byte[fis.available()];
                    fis.read(body);
                    fis.close();
                    response.setResponseBody(body);
                    response.setContentTypeHeader(FileHandler.getContentTypeFromFileExtension(getFile));
                }
                catch(FileNotFoundException ex){
                   return createSimpleResponse(request.getProtocol(), HttpStatus.NOT_FOUND, HtmlMessages.FILE_NOT_FOUND);
                }
                catch(IOException ex){
                    return createSimpleResponse(request.getProtocol(), HttpStatus.INTERNAL_SERVER_ERROR, HtmlMessages.INTERNAL_SERVER_ERROR);
                }
            } else {
                return createSimpleResponse(request.getProtocol(), HttpStatus.NOT_FOUND, HtmlMessages.FILE_NOT_FOUND);
            }
        return response;
    }

    @Override
    public HttpResponse handleHEADRequest(HttpRequest request) {
        File headFile = new File("webapps" + request.getRequestURI());
        HttpResponse response = new HttpResponse();
        response.setProtocol(request.getProtocol());

        HttpResponse badResponse = checkForAbsolutePathTraversal(request, headFile);
        if (badResponse != null) {
            badResponse.setResponseBody(null);
            return badResponse;
        }
        if (headFile.exists()) {
            try {
                response.setHttpStatus(HttpStatus.OK);
                response.setDateHeader(new Date());

                InputStream fis = new FileInputStream(headFile);
                response.setContentLengthHeader(fis.available());
                byte[] body = new byte[fis.available()];
                fis.read(body);
                fis.close();
                response.setResponseBody(null);
                response.setContentTypeHeader(FileHandler.getContentTypeFromFileExtension(headFile));
            } catch (FileNotFoundException ex) {
                response = createSimpleResponse(request.getProtocol(), HttpStatus.NOT_FOUND, HtmlMessages.FILE_NOT_FOUND);
                response.setResponseBody(null);
                return response;
            } catch (IOException ex) {
                response = createSimpleResponse(request.getProtocol(), HttpStatus.INTERNAL_SERVER_ERROR, HtmlMessages.INTERNAL_SERVER_ERROR);
                response.setResponseBody(null);
                return response;
            }
        } else {
            response = createSimpleResponse(request.getProtocol(), HttpStatus.NOT_FOUND, HtmlMessages.FILE_NOT_FOUND);
            response.setResponseBody(null);
            return  response;
        }
        return response;
    }

    @Override
    public HttpResponse handlePOSTRequest(HttpRequest request){

        return null;
    }

    /**
     * Method that process POST request and sends specific HTTP request
     * @param request
     * @return
     */
    @Override
    public HttpResponse handleDELETERequest(HttpRequest request){
        File deleteFile = new File("webapps"+request.getRequestURI());
        HttpResponse response = new HttpResponse();

        HttpResponse badRequest = checkForAbsolutePathTraversal(request, deleteFile);
        if(badRequest!=null){
            return badRequest;
        }
        if(deleteFile.exists()){
            if(deleteFile.delete()){
                return createSimpleResponse(request.getProtocol(), HttpStatus.OK, HtmlMessages.FILE_DELETED);
            }
            else
            {
                return createSimpleResponse(request.getProtocol(), HttpStatus.FORBIDDEN, HtmlMessages.ACCESS_FORBIDDEN);
            }
        }
        else
        {
            return createSimpleResponse(request.getProtocol(), HttpStatus.NOT_FOUND, HtmlMessages.FILE_NOT_FOUND);
        }

    }

    /**
     * Method that handles PUT request and sends specific HTTP response
     * @param request
     * @return
     */
    @Override
    public HttpResponse handlePUTRequest(HttpRequest request){
        File putFile = new File("webapps"+request.getRequestURI());
        HttpResponse response = new HttpResponse();
        response.setProtocol(request.getProtocol());

        HttpResponse badResponse = checkForAbsolutePathTraversal(request, putFile);
        if(badResponse!=null){
            return badResponse;
        }
        if(putFile.exists()){
            return createSimpleResponse(request.getProtocol(),HttpStatus.FORBIDDEN, HtmlMessages.ACCESS_FORBIDDEN);
        }
        else
        {
            try {
                if(putFile.createNewFile()){
                   return createSimpleResponse(request.getProtocol(), HttpStatus.CREATED, HtmlMessages.FILE_CREATED);
                }


            } catch (IOException e) {
                return createSimpleResponse(request.getProtocol(), HttpStatus.INTERNAL_SERVER_ERROR, HtmlMessages.INTERNAL_SERVER_ERROR);
            }
        }
        return response;
    }

    /**
     * Method that sends simple HTTP responses based on a specific scenario
     * @param protocol - mapped protocol from the client's request
     * @param statusCode
     * @param messages
     * @return
     */
    @Override
    public HttpResponse createSimpleResponse(String protocol, HttpStatus statusCode, HtmlMessages messages) {
        HttpResponse response = new HttpResponse();
        response.setProtocol(protocol);
        response.setHttpStatus(statusCode);
        response.setDateHeader(new Date());
        response.getResponseHeaders().put("Connection","close");
        response.setContentTypeHeader(ContentType.HTML);
        response.setContentLengthHeader(messages.getErrorMessage().length());
        response.setResponseBody(messages.getErrorMessage().getBytes());
        return response;
    }

    /**
     * Method that sends the processed HTTP response
     * @param response
     * @param output
     * @throws IOException
     */
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

    /**
     * Method that performs checks for the keep-alive flag
     * @param request
     * @param response
     * @return
     */
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
