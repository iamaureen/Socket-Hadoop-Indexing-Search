package LearningServers;

public class JobAck {
	private String id;
	private String status;
	
	public JobAck(String i, String s) {
		setId(i);
		setStatus(s);
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

}
