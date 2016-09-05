package com.webserver;


import com.webserver.server.Server;
import com.webserver.utils.Settings;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import test.AllTests;

import java.io.File;
import java.io.IOException;

/**
 * Created by Cosmin on 8/24/2016.
 */
public class Application {

    public static void main(String[] args){


        try {
            File f = new File("");
            System.out.println("Current Path: "+f.getAbsolutePath());
            System.out.println("Running Unit Tests...");
            Server server = new Server(Settings.listeningPort,
                    Settings.initialNoThreads,
                    Settings.maxNoThreads);
            server.start();
            Result result = JUnitCore.runClasses(AllTests.class);
            for(Failure fail : result.getFailures()){
                System.err.println("The following unit test failed: "+fail.toString());
            }
            if(result.wasSuccessful()){
                System.out.println("All Unit Tests were executed successfully...");
            }
            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                server.stop();
            }));
        }
        catch(IOException ex){
            ex.printStackTrace();
        }



    }
}
