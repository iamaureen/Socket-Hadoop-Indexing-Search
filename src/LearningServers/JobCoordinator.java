package LearningServers;

import java.util.ArrayList;

public class JobCoordinator extends Thread {

	//This JobCoordinator creates jobs and sends them to registered workers and waits until the job is completed.
	private Job MyJob = null;
	
	public JobCoordinator() {
	
		
	}
	
	public String initJob(boolean indexJob, String criteria) {
		MyJob = new Job(criteria, indexJob);
		if(indexJob) {
			
		}
		else {
			
		}
		
		return MyJob.getId();
	}
	

}
