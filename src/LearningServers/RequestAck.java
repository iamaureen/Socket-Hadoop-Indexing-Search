package LearningServers;

import java.io.Serializable;
import java.util.UUID;

public class RequestAck extends Work implements Serializable{
	private String id;
	private String requestId;
	private String status;

	public RequestAck(String status) {
		id = UUID.randomUUID().toString();
		this.setStatus(status);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
