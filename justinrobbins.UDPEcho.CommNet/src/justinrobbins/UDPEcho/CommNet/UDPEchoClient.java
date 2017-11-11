package justinrobbins.UDPEcho.CommNet;

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPEchoClient {

	private static final int EXIT_FAILURE = 1;

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(System.in);
		InetAddress address;

		// Valid Use Check
		if (args.length != 2) {
			System.err.printf("usage: java UDPEchoClient <host name> <port number>\n");
			System.exit(EXIT_FAILURE);
		}

		// Set address for packets
		String hostName = args[0];

		address = InetAddress.getByName(hostName);

		// Verify port number
		int port = 0;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException NFE) {
			System.err.printf("Error parsing port number\n");
			System.exit(EXIT_FAILURE);
		}

		// Create Datagram Socket
		try (DatagramSocket echoSocket = new DatagramSocket();) {
			byte[] buf = new byte[256];
			DatagramPacket echoPacket;
			String userInput;
			String recOutput;

			// While the user still inputs data, keep sending packets.
			// If the server fails to respond after 30 seconds, end the program.
			System.out.printf("Client: ");
			while ((userInput = in.nextLine()) != null) {

				buf = userInput.getBytes("UTF-8");

				echoPacket = new DatagramPacket(buf, buf.length, address, port);

				echoSocket.send(echoPacket);

				// If server fails to respond after 30 seconds, close program
				echoSocket.setSoTimeout(30000);
				echoSocket.receive(echoPacket);
				echoSocket.setSoTimeout(0);

				recOutput = new String(echoPacket.getData(), "UTF-8");
				System.out.println("Echo: " + recOutput);
				System.out.printf("Client: ");
			}
			in.close();
		} catch (SocketTimeoutException STE) {
			System.out.printf("Server failed to respond in time, closing...\n");
			System.exit(EXIT_FAILURE);
		} catch (UnknownHostException error) {
			System.err.printf("unknown host: %s\n", hostName);
			System.exit(EXIT_FAILURE);
		} catch (IOException error) {
			System.err.printf("unable to   establish I/O connection to %s\n", hostName);
			System.exit(EXIT_FAILURE);
		}
	}
}