package tinyGoogle;

import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
//This class contains some server helper methods to use. 

public class utility {
	private static String jobDir = null;
	private static String basePath = ".";

	public static final int MASTERPORT = 12345;
	public static final String MASTERHOST = "127.0.0.1";// "oxygen.cs.pitt.edu";

	// return the file path when a filename is given
	public static String getFilePath(String filename) {
		String workingDirectoty = System.getProperty("user.dir");
		String fileName = filename;
		String filePath = workingDirectoty + "/src/tinyGoogle/" + fileName;

		System.out.println("Working Directory = " + filePath);

		return filePath;
	}

	public static void setJobDir(String bp) {
		jobDir = bp + "/Jobs";
		new File(jobDir).mkdirs();
	}

	public static String getJobDir() {
		if (jobDir == null) {
			System.err.println("The Job Directory needs to be specified");
			return null;
		}
		return jobDir;
	}

	public static String getBasePath() {
		return basePath;
	}

	public static boolean deleteDirectory(File directoryToBeDeleted) {
		// src: https://www.baeldung.com/java-delete-directory
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ex) {
			return "no host name";
		}
	}

	public static int getConnectedPort(Socket sock) {
		return sock.getPort();
	}

	public static String getConnectedHostName(Socket sock) {
		return sock.getInetAddress().getHostAddress();
	}

}
