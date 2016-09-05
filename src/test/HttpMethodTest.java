package test;

import com.sun.deploy.net.HttpRequest;
import com.webserver.utils.HttpMethod;
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
 * Unit Test class that tests the available HTTP methods: PUT, GET, DELETE
 */
public class HttpMethodTest {

    public void setUp(){

    }
    public int sendHttpRequest(String host, int port, HttpMethod method, String URI){
        int statusCode=0;
        try
        {
            Socket s = new Socket(host,port);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            StringBuilder builder = new StringBuilder();
            builder.append(method.getHttpMethod()+" "+URI +" HTTP/1.0 \r\n");
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
    public void testPUTRequest(){
        Assert.assertEquals("Testing PUT Request",201, sendHttpRequest("localhost",8080, HttpMethod.PUT,"/test/unittest.txt"));
    }
    @Test
    public void testGETRequest(){
        Assert.assertEquals("Testing GET Request ",200,sendHttpRequest("localhost",8080,HttpMethod.GET,"/test/unittest.txt"));
    }
    @Test
    public void testDELETERequest(){
        Assert.assertEquals("Test DELETE Request",200, sendHttpRequest("localhost",8080,HttpMethod.DELETE,"/test/unittest.txt"));
    }
}
