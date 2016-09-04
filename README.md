# Java Thread Pooling Web Server

Description: Implementation of a thread pooled web server that manages the requested files.

# Data Flow

- When the server is started a thread pool is initialized with a specified number of threads that are waiting for an incoming HTTP request;
- The server is parsing the input data, validate it and builds a HTTP Response;
- Before sending the response it performs checks for the `keep-alive` flag:
	-`YES`: The HttpTask will be submitted back in the Thread Pool;
	-`NO`: The socket will be closed; 

