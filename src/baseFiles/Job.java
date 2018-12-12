package baseFiles;

import java.io.Serializable;
import java.util.UUID;

public class Job extends Work implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6046801653781034075L;

	// who where what
	private boolean isIndexJob;
	private String targetValue;
	//target Value is either a the document path or the search terms, depending on if the job is an index or a search
	private String id;
	private String[] workerArray;
	// an array of all the worker names
	private String[] mapTasks;
	// each entry in map tasks is the {worker name,start line, end line} Delimiter is |
	private String[] reduceTasks;
	// each entry in reduce tasks is {worker name, start letter, end letter}

	public Job(String dP, boolean isJobIndex) {
		setTargetValue(dP);
		setId(UUID.randomUUID().toString());

		setIndexJob(isJobIndex);
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isIndexJob() {
		return isIndexJob;
	}

	public void setIndexJob(boolean isIndexJob) {
		this.isIndexJob = isIndexJob;
	}

	public JobAck generateJobAck(String status, String workerName) {
		return new JobAck(this.getId(), status, workerName);
	}

	public String[] getWorkerArray() {
		return workerArray;
	}

	public void setWorkerArray(String[] workerArray) {
		this.workerArray = workerArray;
	}

}
