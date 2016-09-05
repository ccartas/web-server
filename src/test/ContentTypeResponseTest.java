package test;


import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.Socket;


/**
 * Created by Cosmin on 9/3/2016.
 */

/**
 * Unit Test Class used to test the returned Content-Type header from the HTTP Response
 */
public class ContentTypeResponseTest {

    public void setUp(){

    }

    public String sendRequest(String host,int port,String URI){
        String contentType="";
        try
        {
            Socket s = new Socket(host,port);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            StringBuilder builder = new StringBuilder();
            builder.append("GET " +URI +" HTTP/1.0 \r\n");
            builder.append("Host: localhost:8080 \r\n");
            builder.append("Connection: close \r\n");
            builder.append("Cache-Control: max-age=0 \r\n");
            builder.append("Upgrade-Insecure-Requests: 1 \r\n");
            builder.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 \r\n");
            builder.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8 \r\n");
            builder.append("Accept-Encoding: gzip, deflate, sdch \r\n");
            builder.append("Accept-Language: ro-RO,ro;q=0.8,en-US;q=0.6,en;q=0.4 \r\n\r\n");
            writer.write(builder.toString());
            writer.flush();
            InputStream input = s.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line="";
            while((line=reader.readLine())!=null && !line.equals("")){
                if(line.startsWith("Content-Type")){
                    contentType = line.split(": ")[1];
                    return contentType;
                }
            }
           s.close();

        }
            catch(Exception ex){

            }

        return contentType;
    }

    @Test
    public void testGetCssFile(){
        Assert.assertEquals("GET CSS File", "text/css", sendRequest("localhost",8080,"/test/main.css"));
    }
    @Test
    public void testGetHTMLFile(){
        Assert.assertEquals("GET HTML File", "text/html", sendRequest("localhost",8080,"/test/index.html"));
    }
    @Test
    public void testGetJSFile(){
        Assert.assertEquals("GET JS File", "application/js",sendRequest("localhost",8080,"/test/test.js"));
    }
    @Test
    public void testGetJPGFile(){
        Assert.assertEquals("GET JPG File", "image/jpg",sendRequest("localhost",8080,"/test/test1.jpg"));
    }
    @Test
    public void testGetPNGFile(){
        Assert.assertEquals("GET PNG File", "image/png", sendRequest("localhost",8080,"/test/but2.png"));
    }
    @Test
    public void testUnknownFile(){
        Assert.assertEquals("GET Unknown File Type", "text/html", sendRequest("localhost",8080,"/test/file.unk"));
    }
}
