package LearningServers;

import java.io.Serializable;

public class Request extends Work implements Serializable{
	//This is the object that the clients will make and send to the server
	
	private String id;
	
	//of value index or search
	private String requestType;

	private String requestVal;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestVal() {
		return requestVal;
	}

	public void setRequestVal(String requestVal) {
		this.requestVal = requestVal;
	}
	

}
