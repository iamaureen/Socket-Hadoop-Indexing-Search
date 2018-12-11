package tinyGoogle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class IIInterface {
	// A static class to handle the creation of the II structure

	// the base path for where the Inverted Index structured should be
	private static String basePath = "";
	private static String dirs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();

	public static void setupStructure(String bp) {
		basePath = bp;
		
		// create the structure so it exists

		for (int i = 0; i < dirs.length(); i++) {
			new File(basePath + "/InvertedIndex/" + dirs.charAt(i)).mkdirs();
		}

		System.out.println("II directories created");

	}

	public static void deleteStructure() {
		if (basePath.length() == 0) {
			System.out.println("base path is not set, run setupStructure before deleting it");
			return;
		}

		utility.deleteDirectory(new File(basePath + "/InvertedIndex"));

	}

	public static boolean addEntry(String term, int DocId, int count, String path) {
		String targetDir = basePath + "/InvertedIndex/" + dirs.charAt((int) term.charAt(0) - (int) ('a')) + "/";

		try {
			File f = new File(targetDir + term + "-" + DocId + "-" + count + ".txt");
			FileWriter fw = new FileWriter(f);
			BufferedWriter toSave = new BufferedWriter(fw);
			toSave.write(path);
			toSave.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static LinkedList<String> getFileList(char start, char end){
		LinkedList<String> retval = new LinkedList<String>();
		
		for(int i = (int)(start - 'a'); i<(int)(end - 'a'); i++) {
			File directory = new File(getDir(i));
			for(String f: directory.list() ) {
				retval.add(f);
			}
		}
		
		return retval;
		
	}

	public static String getTerm(String fileName) {
		return fileName.substring(0, fileName.indexOf("-"));
	}
	public static String getPath(String fileName) {
		String retval = "nothing";
		
		String path = getDir( (int)fileName.charAt(0)- 'a' ) + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			retval =  br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return retval;
	}
	private static String getDir(int i) {
		return basePath + "/InvertedIndex/" + dirs.charAt(i) + "/";
	}

	public static void main(String[] args) {
		try {
			System.out.println(new File("./InvertedIndex").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//This creates the structure at the specified directory (note no slash at the end)
		IIInterface.setupStructure(".");
		
		//These add all of the files to the II structure
		IIInterface.addEntry("aardvark", 1, 25, "path/to/file");
		IIInterface.addEntry("qardvark", 1, 25, "path/sto/file");
		IIInterface.addEntry("aardvark", 1, 245, "paths/to/file");
		IIInterface.addEntry("rardvark", 2, 235, "path/tso/file");
		IIInterface.addEntry("aardvark", 1, 525, "path/to/file");
		IIInterface.addEntry("vardvark", 8, 25, "path/to/sfile");
		IIInterface.addEntry("aardvark", 1, 325, "path/to/file");
		IIInterface.addEntry("sardvark", 2, 255, "path/to/file");
		IIInterface.addEntry("aardvark", 5, 235, "path/to/file");
		
		//This is how the workers can get a list of the files in the specified directory range
		LinkedList<String> files = IIInterface.getFileList('a', 'z');
		
		
		//the get path function returns the location of the indexed document
		for(String s: files) {
			System.out.println(s + ":" +IIInterface.getPath(s));
		}
		
		//The code below the III structure.
		//IIInterface.deleteStructure();
		
		

	}
}
