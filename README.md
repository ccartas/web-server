# Java Thread Pooling Web Server

Description: Implementation of a thread pooled web server that manages the requested files.

# Requirements

- Java 8 http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html (The implementation contains lambda and streams which are available only in JAVA 8);

# How 2 Run

- Windows:

	+ Set `JAVA_HOME` Environment variable to your java installation folder (e.g. `C:\Program Files\Java\jdk1.8.0_40\`);
	+ Add `java bin folder to Path` (e.g. `C:\Program Files\Java\jdk1.8.0_40\bin`);
	+ Run from `Command Line` the `startup.bat` located in the project root folder;

- Linux:

	+ Set `JAVA_HOME` Environment variable (`sudo export JAVA_HOME=/usr/lib/jvm/jdk1.8.0`);
	+ Add `java bin folder to Path` (`sudo export PATH=$PATH:$JAVA_HOME/bin`);
	+ Run from `Terminal` the `startup.sh` located in the project root folder;

# Data Flow

- When the server is started a thread pool is initialized with a specified number of threads that are waiting for an incoming HTTP request;
- The server is parsing the input data, validate it and builds a HTTP Response;
- Before sending the response it performs checks for the `keep-alive` flag:
	+ `YES`: The HttpTask will be submitted back in the Thread Pool;
	+ `NO`: The socket will be closed; 

# Approach:

- In order to create the thread pool ThreadPoolExecutor API has been chosen:
	
	## ThreadPoolExecutor vs ExecutorService:

	+ `ExecutorService` is using `ThreadPoolExecutor` in its `newFixedThreadPool(int numberOfThreads)` method, but it doesn't match the requirements for a HTTP `keep-alive` flag. `ThreadPoolExecutor` constructor provides a `keepAlive` parameter which specifies the time a thread could stay idle after finishing executing the task.
	+ If all the threads from the the Thread Pool are executing a task, the incoming tasks will be added into a `BlockingQueue`. If the `BlockinQueue` is filled and more tasks are comming the server will throw `RejectedExecutionException`. A custom `RejectedExecutionHandler` has been implemented in order to keep retrying to add the rejected task back to the `BlockingQueue`.

- For proper error handling custom exceptions were created and thrown:
	
	## Exception Handling Cases:

	+ `InvalidRequestException` is thrown when the InputStream contains `null` data or if the first line of the Request is not valid;
	+ `InvalidProtocolException` is thrown when the protocol mentioned in the request is not `HTTP`;
	+ `HttpHeaderFormatException` is thrown when the HTTP Header does not have the following format `Header_Name: Header_Value`;

- Supported HTTP Responses:

	## Supported HTTP Methods:

	+ `GET`: You can get any type of file that is located in the `webapps` folder;
	+ `PUT`: Creates a file specified in the `Request URI`. If the file already exists, the server will send `FORBIDDEN` status code.
	+ `DELETE`: Deletes a file specified in the `Request URI`. If the file cannot be deleted the server will send `FORBIDDEN` status code.
	+ `POST`: Not implemented. (Couldn't think at a valid use case. Create a file from the request body parametter???)
	
	## HTTP Status Code:

	+ `200 OK`;
	+ `400 Bad Request`;
	+ `403 Forbidden`;
	+ `404 Not Found`;
	+ `500 Internal Server Error`;

	## Supported Contet Type:

	+ 'text/html'; `text/plain`; `text/css`; `application/js`; `image/png`; `image/jpg`; `image/jpeg`;
	+ If the content type of the requested file is not reconized the header will be asigned with the value: `Content-Type: text/html`;

# References:

- Git repository: https://github.com/ibogomolov/WebServer
- How `keep-alive` works: http://stackoverflow.com/questions/20763999/how-does-http-keep-alive-work;
- How `ThreadPoolExecutor` works: https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ThreadPoolExecutor.html;
- When `RejectedExecutionException` is thrown: http://stackoverflow.com/questions/4172674/rejectedexecutionexception-in-java-threads;
- http://www.javaworld.com/article/2853780/java-app-dev/socket-programming-for-scalable-systems.html

# Other approach:

- Non Blocking Input-Output mechanism (NodeJS like);

