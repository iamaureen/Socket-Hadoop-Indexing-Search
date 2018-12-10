package LearningServers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class Master {

	public static List<MasterConnectionThread> WorkerList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());
	public static List<MasterConnectionThread> ClientList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());
	
	public static LinkedBlockingQueue<Object> WorkQueue = new LinkedBlockingQueue<Object>();
		//contains Request Objects and Job Objects
		//
	public final static int PORT = 12345;

	public static void main(String[] args) {
		new MasterServerThread().start();
		
		//loop through a queue to handle requests
		while(true) {
			//handle request
			//TODO change this to handle work distribution
			//If it is a Request send the job to worksers
			//if it is a job complete send the result to clients
			try {
				System.out.println(WorkQueue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
