package LearningServers;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerBase {

	// This is a worker base and is supposed to template how a worker is supposed to
	// handle communication.

	// each worker will will have a server to communicate with the other servers

	// mapper will become server
	public static String workerName = "w-" + UUID.randomUUID().toString();
	public static LinkedBlockingQueue<Object> JobQueue = new LinkedBlockingQueue<Object>();

	public static void main(String[] args) {
		//first thing to do is connect to the master server
		//and we will have a thread for that
		new WorkerToMasterThread().start();
		
		// we will now enter the work loop
		
		while(true) {
			
			try {
				Job ActiveJob  = (Job)JobQueue.take();
				
				if(ActiveJob.isIndexJob()) {
					String mapTask = getMapTask(ActiveJob.getMapTasks());
					if(mapTask == null) {
						continue;
					}
					
					String[] content = mapTask.split("|");
					int start = Integer.parseInt(content[1]);
					int end = Integer.parseInt(content[2]);
					
					IndexMap(ActiveJob.getTargetValue(), start, end);
					
				}else {
					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

	private static String getMapTask(String[] mapTasks) {
		for (String s : mapTasks) {
			if (s.contains(workerName)) {
				return s;
			}
		}
		System.err.println("Worker " + workerName + " got a job it wasn't supposed to");
		return null;
	}

	public static void IndexMap(String path, int start, int end) {
		

	}

}
