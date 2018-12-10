package LearningServers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterServerThread extends Thread {

	public MasterServerThread() {
	}

	public void run() {
		ServerSocket serverSock = null;
		Socket sock = null;

		try {
			serverSock = new ServerSocket(Master.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("started server");
		while (true) {

			try {
				sock = serverSock.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// new thread for a client and worker input and output.
			new MasterConnectionThread(sock).start();
		}
	}
}
