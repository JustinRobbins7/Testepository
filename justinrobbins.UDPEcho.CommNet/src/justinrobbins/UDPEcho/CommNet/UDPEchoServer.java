package justinrobbins.UDPEcho.CommNet;

import java.io.*;
import java.net.*;

public class UDPEchoServer {

	private static final int EXIT_FAILURE = 1;

	public static void main(String[] args) throws IOException {

		// Valid Use identification
		if (args.length != 1) {
			System.err.printf("usage: java UDPEchoServer <port number>\n");
			System.exit(EXIT_FAILURE);
		}

		// Port Number error handling
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException NFE) {
			System.err.printf("Error parsing port number\n");
			System.exit(EXIT_FAILURE);
		}

		// Open a DatagramSocket and listen for a client
		try (DatagramSocket echoUDPServ = new DatagramSocket(port);) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			// Listen for the first packet forever, loop ends when client
			// fails to send a message for 30 seconds.
			echoUDPServ.receive(packet);
			while (true) {
				InetAddress address = packet.getAddress();
				int cPort = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, cPort);
				echoUDPServ.send(packet);

				echoUDPServ.setSoTimeout(30000);
				echoUDPServ.receive(packet);
				echoUDPServ.setSoTimeout(0);
			}
		} catch (SocketTimeoutException STE) {
			System.out.printf("Client failed to respond in time, closing...\n");
			System.exit(EXIT_FAILURE);

		} catch (IOException IOE) {
			System.out.printf("error when trying to   listen on port %d or listening for connection\n", port);
			System.exit(EXIT_FAILURE);
		}
	}
}
