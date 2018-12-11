package LearningServers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Master {

	public static List<MasterConnectionThread> WorkerList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());
	public static List<MasterConnectionThread> ClientList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());

	public static ConcurrentLinkedQueue<Object> WorkQueue = new ConcurrentLinkedQueue<Object>();
	
	public static Map<String, JobCoordinator> jobMap = Collections.synchronizedMap(new HashMap<String, JobCoordinator>());
	
		//contains Request Objects and Job Objects
		//

	//I need some concept of a current job or active set of jobs
	
	public static void main(String[] args) {
		
		
		//create the server
		new MasterServerThread().start();
		//create the II structure
		
		
		//loop through a queue to handle requests
		
		
		while(true) {
			//handle request
			//TODO change this to handle work distribution
			//If it is a Request send the job to worksers
				//JobCoordinater.init
				//jobcoordinater.Send and wait
				//jobcoordinater.run
			//if it is a job complete send the result to clients
			
				System.out.println(WorkQueue.poll());
		
		}
		

	}

	public static void addConnection(MasterConnectionThread masterConnectionThread, String line) {
		if (line.startsWith("w-") && !WorkerList.contains(masterConnectionThread)) {
			WorkerList.add(masterConnectionThread);
		}
		if (line.equalsIgnoreCase("client") && !ClientList.contains(masterConnectionThread)) {
			ClientList.add(masterConnectionThread);
		}
	}
	
	public static MasterConnectionThread getMCT(String workerName) {
		for(MasterConnectionThread mct: WorkerList) {
			if(mct.getConnectedName().equals(workerName)) {
				return mct;
			}
		}
		return null;
	}
	

	public static void removeConnection(MasterConnectionThread masterConnectionThread, String connectedName) {
		if (connectedName.equalsIgnoreCase("worker") && !WorkerList.contains(masterConnectionThread)) {
			WorkerList.remove(masterConnectionThread);
		}
		if (connectedName.equalsIgnoreCase("client") && !ClientList.contains(masterConnectionThread)) {
			ClientList.remove(masterConnectionThread);
		}		
	}
}
