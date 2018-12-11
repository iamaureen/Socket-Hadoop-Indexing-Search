package LearningServers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import tinyGoogle.FileHandler;

public class JobCoordinator extends Thread {

	// This JobCoordinator creates jobs and sends them to registered workers and
	// waits until the job is completed.
	private Job MyJob = null;
	private Queue<Object> inbox;
	private Queue<Object> initInbox;
	public JobCoordinator() {
		this.inbox = new ConcurrentLinkedQueue<Object>();
		this.initInbox = new ConcurrentLinkedQueue<Object>();
		

	}
	public boolean placeInInbox(Object toReceive) {
		try {
			return this.inbox.add(toReceive);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean placeInInitInbox(Object toReceive) {
		try {
			return this.initInbox.add(toReceive);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public String initJob(boolean indexJob, String criteria) throws IOException {
		MyJob = new Job(criteria, indexJob);
		ArrayList<Integer> workerIndex = genNumbers(Master.WorkerList.size());
		String[] charSplits = genCharSplits(workerIndex.size());
		HashSet<String> JobWorkers = new HashSet<String>();
		// this is an index request
		if (indexJob) {
			int numLines = FileHandler.countLines(criteria);
			int numWorkers = Math.min(numLines / 100, Master.WorkerList.size());
			int numReducers = charSplits.length;

			// gen Map tasks
			Collections.shuffle(workerIndex);
			String[] mapTasks = new String[numWorkers];
			int linesPerWorker = numLines / numWorkers;
			for (int i = 0; i < numWorkers; i++) {
				
				String workerName = Master.WorkerList.get(workerIndex.get(i)).getConnectedName();
				String temp = workerName + "|" + i * linesPerWorker + "|" + (i * linesPerWorker + linesPerWorker - 1);
				mapTasks[i] = temp;
				JobWorkers.add(workerName);
			}

			MyJob.setMapTasks(mapTasks);

			// gen Reduce tasks
			Collections.shuffle(workerIndex);

			String[] reduceTasks = new String[numReducers];

			for (int i = 0; i < numReducers; i++) {
				String workerName = Master.WorkerList.get(workerIndex.get(i)).getConnectedName();
				String temp = workerName + "|" + charSplits[i].charAt(0) + "|" + charSplits[i].charAt(1);
				reduceTasks[i] = temp;
				JobWorkers.add(workerName);
			}

			MyJob.setReduceTasks(reduceTasks);

		}
		// This is a search request
		else {

			int numWorkers = charSplits.length;
			int numReducers = 1;

			Collections.shuffle(workerIndex);
			String[] mapTasks = new String[numWorkers];
			for (int i = 0; i < numReducers; i++) {
				String workerName = Master.WorkerList.get(workerIndex.get(i)).getConnectedName();
				String temp = workerName + "|" + charSplits[i].charAt(0) + "|" + charSplits[i].charAt(1);
				mapTasks[i] = temp;
				JobWorkers.add(workerName);
			}
			
			MyJob.setMapTasks(mapTasks);
			
			String[] reduceTasks = new String[numReducers];
			reduceTasks[0] = Master.WorkerList.get(workerIndex.get(0)).getConnectedName() + "|" + "a"+ "|" + "z";
			JobWorkers.add(Master.WorkerList.get(workerIndex.get(0)).getConnectedName());

		}
		
		String[] workerArr = new String[JobWorkers.size()];
		JobWorkers.toArray(workerArr);
		
		MyJob.setWorkerArray(workerArr);
		

		return MyJob.getId();
	}

	private String[] genCharSplits(int size) {
		String[] split1 = { "az" };
		String[] split2 = { "al", "mz" };
		String[] split3 = { "ah", "is", "tz" };
		String[] split4 = { "ae", "fl", "ms", "tz" };

		if (size >= 4) {
			return split4;

		}
		if (size == 3) {
			return split3;
		}
		if (size == 2) {
			return split2;
		} else {
			return split1;
		}

	}

	private ArrayList<Integer> genNumbers(int size) {
		ArrayList<Integer> retval = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			retval.add(i);
		}
		return retval;
	}
	
	//this method is to make sure that there is not deadlock on workers getting work from other jobs before this one is started.

	public void sendJobsAndWaitAck() {
		//send job info to relevant workers
		HashMap<String, Integer> workersToWait = new HashMap<String, Integer>();
		for (String worker: MyJob.getWorkerArray()) {
			MasterConnectionThread mct = Master.getMCT(worker);
			boolean success = false;
			do {
				// keep trying to place into outbox until it succeeds
				success = mct.placeInOutbox(MyJob);
			} while (!success);
			workersToWait.put(worker, 0);
		}
		
		//wait on initInbox to receive all requests.
		JobAck ja = null;
		int done = getPercent(workersToWait);
		while(done < 1) {
			ja = (JobAck) this.initInbox.poll();

			if (ja != null) {
				workersToWait.put(ja.getWorkerName(), 1);
			}
			done = getPercent(workersToWait);
		}
		
	}

	private int getPercent(HashMap<String, Integer> workersToWait) {
		int total =0,sum=0;
		for(String key: workersToWait.keySet()) {
			total += 1;
			sum += workersToWait.get(key);
		}
		return sum/total;
	}
	
	
	// wait for work to complete
	public void run() {
		
		
	}

}
