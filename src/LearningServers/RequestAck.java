package LearningServers;

import java.util.UUID;

public class RequestAck extends Work {
	private String id;
	private String requestId;
	private String status;

	public RequestAck(String status) {
		id = UUID.randomUUID().toString();
		this.status = status;
	}
}
