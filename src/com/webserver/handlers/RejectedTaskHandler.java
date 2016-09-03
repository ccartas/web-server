package com.webserver.handlers;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Cosmin on 8/26/2016.
 */

/**
 * # If the ThreadPoolExecutor BlockingQueue is filled and a new task is submitted to execute the ThreadPool will throw RejectedExceptionException
 * # In order to try to execute the tasks that were not added to the BlockingQueue, RejectedTaskHandler implementation was required
 */
public class RejectedTaskHandler implements RejectedExecutionHandler {


    /**
     * Method for handling the rejected tasks. It tries to add the rejected Task back in the queue.
     * @param r
     * @param executor
     *
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if(!executor.isShutdown()){
           try {
               executor.getQueue().put(r);
           }
           catch(InterruptedException ex){
               ex.printStackTrace();
           }
       }
    }
}
