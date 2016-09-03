package com.webserver.handlers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Cosmin on 8/26/2016.
 */
/**
    ThreadPoolExecutor with custom behaviour
* */

public class ServerThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * ServerThreadPoolExecutor Constructor:
     * @param corePoolSize - The number of threads present in the thread pool after initialization;
     * @param maximumPoolSize - Maximum number of threads RUNNING in the thread pool;
     * @param keepAliveTime - Idle time of a thread after finishing executing 1 task;
     * @param unit - Time unit for the keepAliveParameter;
     * @param workQueue - If all the threads are executing a task the waiting tasks will be put in the BlockingQueue;
     * */
    public ServerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void beforeExecute(Thread t, Runnable r){
        super.beforeExecute(t,r);
    }

    @Override
    public void afterExecute(Runnable r, Throwable t){
        super.afterExecute(r,t);
        if(t != null){
            t.printStackTrace();
        }
    }
}
