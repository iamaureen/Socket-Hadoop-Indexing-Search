package LearningServers;

import java.io.Serializable;
import java.util.UUID;

public class Job implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6046801653781034075L;

	// who where what
	@SuppressWarnings("unused")
	private boolean isIndexJob;
	private String targetValue;
	//target Value is either a the document path or the search terms, depending on if the job is an index or a search
	private String jobId;
	private String[] workerArray;
	// an array of tall the worker names
	private String[] mapTasks;
	// each entry in map tasks is the {worker index,start bit index, end bit index}
	private String[] reduceTasks;
	// each entry in reduce tasks is {worker, start letter, end letter}

	public Job(String dP, boolean isJobIndex) {
		setTargetValue(dP);
		setJobId(UUID.randomUUID().toString());

		isIndexJob = isJobIndex;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String[] getWorkerArray() {
		return workerArray;
	}

	public void setWorkerArray(String[] workerArray) {
		this.workerArray = workerArray;
	}

	public String[] getMapTasks() {
		return mapTasks;
	}

	public void setMapTasks(String[] mapTasks) {
		this.mapTasks = mapTasks;
	}

	public String[] getReduceTasks() {
		return reduceTasks;
	}

	public void setReduceTasks(String[] reduceTasks) {
		this.reduceTasks = reduceTasks;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
