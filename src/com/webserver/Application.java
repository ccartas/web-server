package com.webserver;


import com.webserver.server.Server;
import com.webserver.utils.Settings;

import java.io.File;
import java.io.IOException;

/**
 * Created by Cosmin on 8/24/2016.
 */
public class Application {

    public static void main(String[] args){
        try {
            Server server = new Server(Settings.listeningPort,
                    Settings.initialNoThreads,
                    Settings.maxNoThreads);
            server.start();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }



    }
}
