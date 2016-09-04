# Java Thread Pooling Web Server

Description: Implementation of a thread pooled web server that manages the requested files.

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
