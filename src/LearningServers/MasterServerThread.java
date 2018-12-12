package LearningServers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tinyGoogle.utility;

public class MasterServerThread extends Thread {

	public MasterServerThread() {
		
	}

	public void printstmt() {
		System.out.println("Started Master Server");
	}
	
	private void handleConn(Socket sock) {
		// new thread for a client and worker input and output.
		new MasterConnectionThread(sock).start();
	}
	
	public void run() {
		ServerSocket serverSock = null;
		Socket sock = null;

		try {
			serverSock = new ServerSocket(utility.MASTERPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.printstmt();
		while (true) {

			try {
				sock = serverSock.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			handleConn(sock);
		}
	}
}
