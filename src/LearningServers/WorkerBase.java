package LearningServers;

import java.util.UUID;

public class WorkerBase {

	//This is a worker base and is supposed to template how a worker is supposed to handle communication.
	
	//each worker will will have a server to communicate with the other servers
	
	public String workerName = "w-" +UUID.randomUUID().toString();
	
	public static void main(String[] args) {
		
	}

}
