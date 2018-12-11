package tinyGoogle;

public class utility {

	//return the file path when a filename is given
	public static String getFilePath(String filename) {
	 	String workingDirectoty = System.getProperty("user.dir");
		String fileName = filename;
		String filePath = workingDirectoty+"/src/tinyGoogle/" + fileName;
		
		System.out.println("Working Directory = " + filePath);
		
		return filePath;
	}
	
}
