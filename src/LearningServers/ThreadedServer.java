package LearningServers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
	public final static int PORT = 12345;

	public static void main(String[] args) {
		ServerSocket serverSock = null;
		Socket sock = null;

		try {
			serverSock = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("started server");
		while (true) {
			try {
				sock = serverSock.accept();
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
			}
			// new thread for a client
			new EchoThread(sock).start();
		}

	}
}
