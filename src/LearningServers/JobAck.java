package LearningServers;

import java.io.Serializable;
import java.util.UUID;

public class JobAck extends Work implements Serializable{
	private String id;
	private String jobId;
	private String status;
	private String workerName;

	public JobAck(String i, String s, String workerName) {
		this.id = UUID.randomUUID().toString();
		setJobId(i);
		setStatus(s);
		setWorkerName(workerName);

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String id) {
		this.jobId = id;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workName) {
		this.workerName = workName;
	}

}
