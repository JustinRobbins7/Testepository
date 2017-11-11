UDPEchoClient:
	Usage: java UDPEchoClient <hostname> <port number>
	
UDPEchoServer:
	Usage: java UDPEchoServer <port number> 
	
Note on Server Timeouts:
	-After the initial received packet, the server will time out 
	 after thirty seconds if it does not receive a new message.
	 
	-After sending a message, the client times out after thirty 
	 seconds if it does not receive a response.
	