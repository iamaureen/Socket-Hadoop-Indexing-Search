package LearningServers;

public class Work {
	//template object to represent the work to be done
	
	private String id;
	private MasterConnectionThread mct;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MasterConnectionThread getMct() {
		return mct;
	}

	public void setMct(MasterConnectionThread mct) {
		this.mct = mct;
	}

}
