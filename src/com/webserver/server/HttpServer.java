package com.webserver.server;

import java.net.Socket;

/**
 * Created by Cosmin on 8/29/2016.
 */

/**
 * Interface used to handle server specific methods
 */
public interface HttpServer {


    public void start();
    public void stop();
    public void submitRequest(Socket client);

}
