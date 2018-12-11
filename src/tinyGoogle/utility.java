package tinyGoogle;

import java.io.File;

public class utility {
	private static String jobDir = null;

	//return the file path when a filename is given
	public static String getFilePath(String filename) {
	 	String workingDirectoty = System.getProperty("user.dir");
		String fileName = filename;
		String filePath = workingDirectoty+"/src/tinyGoogle/" + fileName;
		
		System.out.println("Working Directory = " + filePath);
		
		return filePath;
	}

	public static void setJobDir(String bp) {
		jobDir = bp+"/Jobs";
		new File(jobDir).mkdirs();
	}
	public static String getJobDir() {
		if(jobDir == null) {
			System.err.println("The Job Directory needs to be specified");
			return null;
		}
		return jobDir;
	}
	
}
