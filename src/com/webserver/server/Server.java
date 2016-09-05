package com.webserver.server;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.webserver.handlers.RejectedTaskHandler;
import com.webserver.handlers.ServerThreadPoolExecutor;
import com.webserver.http.HttpTask;
import com.webserver.utils.Settings;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

/**
 * Created by Cosmin on 8/24/2016.
 */
public class  Server implements HttpServer {

    private volatile boolean running=false;
    private BlockingQueue<Runnable> taskQueue;
    private final ServerSocket serverSocket;
    private final ServerThreadPoolExecutor threadPool;
    private final ExecutorService mainThread;


    /**
     * Server Constructor:
     * @param port - The opened port that server listens on;
     * @param coreThreads - Number of threads that are kept in the pool;
     * @param maxThreads - Maximum number of threads that can be RUNNING in the pool;
     * */
    public Server(int port,int coreThreads, int maxThreads) throws IOException{
        serverSocket = new ServerSocket(port);
        taskQueue = new ArrayBlockingQueue<>(maxThreads);
        threadPool = new ServerThreadPoolExecutor(coreThreads, maxThreads, 7000, TimeUnit.MILLISECONDS, taskQueue);
        RejectedTaskHandler rejectedTaskHandler = new RejectedTaskHandler();
        threadPool.setRejectedExecutionHandler(rejectedTaskHandler);
        mainThread = Executors.newSingleThreadExecutor();
    }

    @Override
    public void start(){
        running = true;
        System.out.println("Server started on the port: "+ Settings.listeningPort);
        this.mainThread.execute(()->{
            while(running){
               try{
                   Socket socket = serverSocket.accept();
                   submitRequest(socket);
               }
               catch(SocketException ex){
                   ex.printStackTrace();
               }
               catch(IOException ex){
                   ex.printStackTrace();
               }
           }
        });
    }

    @Override
    public void stop() {
        try {
            running = false;
            this.mainThread.shutdown();
            this.threadPool.shutdown();
            this.serverSocket.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void submitRequest(Socket client) {
        threadPool.execute(new HttpTask(client, this));
    }


}
