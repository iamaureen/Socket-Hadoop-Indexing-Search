package LearningServers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import tinyGoogle.DocumentIndexer;
import tinyGoogle.IIInterface;
import tinyGoogle.utility;


public class Master {

	public static List<MasterConnectionThread> WorkerList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());
	public static List<MasterConnectionThread> ClientList = Collections
			.synchronizedList(new ArrayList<MasterConnectionThread>());

	public static ConcurrentLinkedQueue<Request> WorkQueue = new ConcurrentLinkedQueue<Request>();
	
	//maps the job ids to the appropriate job coordinator.
	public static Map<String, JobCoordinator> jobMap = Collections.synchronizedMap(new HashMap<String, JobCoordinator>());
	public static String currentRequestType = "";
		//contains Request Objects and Job Objects
		//

	//I need some concept of a current job or active set of jobs
	
	public static void main(String[] args) {
		
		//create the II structure
		IIInterface.setupStructure(".");
		utility.setJobDir(".");
		DocumentIndexer Dind = new DocumentIndexer();
		
		//create the server
		new MasterServerThread().start();

		
		
		
		//loop through a queue to handle requests
		
		
		Request toHandle = null;
		boolean pollAgain = true;
		while(true) {
			//handle request
			
			toHandle = pollAgain ? WorkQueue.poll() : toHandle;
			
			if(pollAgain) {
				pollAgain = true;
			}
			
			if(toHandle != null) {
				//check if I need to do anything
				//if the active request is index and this request is search then don't do anything, vice versa
				if(!toHandle.getRequestType().equals(currentRequestType) && !currentRequestType.equals("")) {
					pollAgain = false;
					continue;
				}
				
				//if I get here then I can process the request because the current request is either the same or none
				
				//check if we got an index request for a document that already exists.
				if(toHandle.getRequestType().equals("index")) {
					String docpath = toHandle.getRequestVal();
					int id = Dind.isDocumentPresentByPath(docpath);
					//this file already exists if the id is less than the max id
					if(id<Dind.getMax()) {
						//if so send a request ack back saying this file was already indexed
						toHandle.getMct().placeInOutbox(new RequestAck("This file was already indexed"));
						//skip rest of logic
						continue;
					}
					
					Dind.updateStruct();
				}
				
				//set the current request
				currentRequestType = toHandle.getRequestType();
				JobCoordinator jc = new JobCoordinator();
				
				try {
					//initialize the job coordinator
					String JobID = jc.initJob(toHandle.getRequestType().equals("index"), toHandle.getRequestVal(), toHandle.getMct());
					//add the job to the map
					jobMap.put(JobID, jc);
					//send out the job requests
					jc.sendJobsAndWaitAck();
					
					//start the thread and continue working
					jc.start();
					
					
				} catch (IOException e) {
					System.err.println("For some reson the file could not be read");
					e.printStackTrace();
				}
				
				
				
			}
			//if map empty then I should resent the request type
			if(jobMap.keySet().size() == 0) {
				currentRequestType = "";
			}
			
		
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
