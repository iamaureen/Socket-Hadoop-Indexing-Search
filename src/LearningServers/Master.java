package LearningServers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Master {

	public static List<MasterConnectionThread> WorkerList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());
	public static List<MasterConnectionThread> ClientList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());
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
			serverSock.
			try {
				sock = serverSock.accept();
			} catch (IOException e) {
				System.out.println("I/O error: " + e);
			}
			// new thread for a client
			new MasterConnectionThread(sock).start();
		}

	}

	public static void addConnection(MasterConnectionThread masterConnectionThread, String line) {
		if (line.equalsIgnoreCase("worker") && !WorkerList.contains(masterConnectionThread)) {
			WorkerList.add(masterConnectionThread);
		}
		if (line.equalsIgnoreCase("client") && !ClientList.contains(masterConnectionThread)) {
			ClientList.add(masterConnectionThread);
		}
	}
}
