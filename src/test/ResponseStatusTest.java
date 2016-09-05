package test;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Cosmin on 9/4/2016.
 */

/**
 * Unit Test class that test the available HTTP Status Codes
 */
public class ResponseStatusTest {

    public void setUp(){

    }
    public int sendInvalidRequestException(String host, int port, String URI){
        int statusCode=0;
        try
        {
            Socket s = new Socket(host,port);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            StringBuilder builder = new StringBuilder();
            builder.append("GET " +URI +" HTTP/1.0  something\r\n");
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
            if((line=reader.readLine())!=null && !line.equals("")){
                statusCode = Integer.valueOf(line.split(" ")[1]);
            }
            s.close();

        }
        catch(Exception ex){

        }

        return statusCode;
    }
    public int sendInvalidProtocolException(String host, int port, String URI){
        int statusCode=0;
        try
        {
            Socket s = new Socket(host,port);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            StringBuilder builder = new StringBuilder();
            builder.append("GET " +URI +" HTTTP/1.0 \r\n");
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
            if((line=reader.readLine())!=null && !line.equals("")){
                statusCode = Integer.valueOf(line.split(" ")[1]);
            }
            s.close();

        }
        catch(Exception ex){

        }

        return statusCode;
    }
    public int sendHttpHeaderFormatException(String host, int port, String URI){
        int statusCode=0;
        try
        {
            Socket s = new Socket(host,port);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            StringBuilder builder = new StringBuilder();
            builder.append("GET " +URI +" HTTP/1.0  something\r\n");
            builder.append("Host: localhost:8080 \r\n");
            builder.append("Connection: close \r\n");
            builder.append("Cache-Control: max-age=0 : sdada \r\n");
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
            if((line=reader.readLine())!=null && !line.equals("")){
                statusCode = Integer.valueOf(line.split(" ")[1]);
            }
            s.close();

        }
        catch(Exception ex){

        }

        return statusCode;
    }
    public int sendRequest(String host,int port,String URI){
        int statusCode=0;
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
            if((line=reader.readLine())!=null && !line.equals("")){
                statusCode = Integer.valueOf(line.split(" ")[1]);
            }
            s.close();

        }
        catch(Exception ex){

        }

        return statusCode;
    }

    @Test
    public void testOKResponse(){
        Assert.assertEquals("GET Existing File", 200, sendRequest("localhost",8080,"/test/main.css"));
    }
    @Test
    public void testForbiddenResponse(){
        Assert.assertEquals("Get Forbidden File",403, sendRequest("localhost",8080,"/../README.md"));
    }
    @Test
    public void testNotFoundResponse(){
        Assert.assertEquals("Get Non Existing File",404, sendRequest("localhost", 8080,"/favicon.ico"));
    }
    @Test
    public void testInvalidRequestException(){
        Assert.assertEquals("Invalid Request Exception ",400, sendInvalidRequestException("localhost",8080,"/test/main.css"));
    }
    @Test
    public void testInvalidProtocolException(){
        Assert.assertEquals("Invalid Protocol Exception ",400, sendInvalidProtocolException("localhost",8080,"/test/but2.png"));
    }
    @Test
    public void testHttpHeaderFormatException(){
        Assert.assertEquals("Invalid HTTP Header Exception ",400, sendHttpHeaderFormatException("localhost",8080,"/test/test.js"));
    }
}
