package LearningServers;

import tinyGoogle.WordCount;

public class MidJob {
	private Job jobref;
	private WordCount wc;
	
	public MidJob(Job j, WordCount w) {
		setJobref(j);
		setWc(w);
		
	}

	public WordCount getWc() {
		return wc;
	}

	public void setWc(WordCount wc) {
		this.wc = wc;
	}

	public Job getJobref() {
		return jobref;
	}

	public void setJobref(Job jobref) {
		this.jobref = jobref;
	}
	
}
