package LearningServers;

public class JobAck extends Work{
	private String id;
	private String status;
	private String workerName;
	
	public JobAck(String i, String s, String workerName) {
		setId(i);
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

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workName) {
		this.workerName = workName;
	}

}
