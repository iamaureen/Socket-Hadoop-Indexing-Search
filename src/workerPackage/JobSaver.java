package workerPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import baseFiles.WordCount;
import baseFiles.utility;

public class JobSaver {

	public static void saveWC(String id, String start, String end, String workerName, WordCount toSave) {
		String path = utility.getJobDir() + "/" + id;
		
		//create a lock file so that the main file is not read from until it is deleted
		File lockFile = new File(path+"/"+ start + "-" + end + "-" + workerName + ".wcrilock");
		try {
			
			lockFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {
			FileOutputStream fos = new FileOutputStream(
					new File(path + "/" + start + "-" + end + "-" + workerName + ".wcri"));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(toSave);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lockFile.delete();

	}

	public static int countWC(String id, String start, String end) {
		String path = utility.getJobDir() + "/" + id;
		File thisJobDir = new File(path);

		String checkString = start + "-" + end;

		int retval = 0;

		for (String p : thisJobDir.list()) {
			if (p.startsWith(checkString)) {
				
				retval += 1;
				if(p.endsWith(".wcrilock")) {
					return 0;
				}
			}
		}

		return retval;
	}

	public static WordCount[] collectWC(String id, String start, String end, int arrSize) {
		String path = utility.getJobDir() + "/" + id;
		File thisJobDir = new File(path);

		String checkString = start + "-" + end;

		WordCount[] retval = new WordCount[arrSize];

		int index = 0;
		for (String p : thisJobDir.list()) {
			if (p.startsWith(checkString)) {
				retval[index] = readWC(path + "/" + p);
				index += 1;
			}
		}

		return retval;
	}

	private static WordCount readWC(String file) {

		try {
			FileInputStream fis = new FileInputStream(new File(file));
			ObjectInputStream ois = new ObjectInputStream(fis);
			WordCount retval = (WordCount) ois.readObject();
			ois.close();
			fis.close();
			return retval;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void saveSearchList(String id, ArrayList<String> termsToSave, String workerName) {
		String path = utility.getJobDir() + "/" + id;

		try {
			FileWriter fos = new FileWriter(new File(path + "/" + workerName + ".txt"));
			fos.write("\n\n");
			for (String line : termsToSave) {
				fos.write(line + "\n");
			}
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static int countSearchList(String id) {

		String path = utility.getJobDir() + "/" + id;
		File thisJobDir = new File(path);

		return thisJobDir.list().length;
	}

	public static ArrayList<String> collectSearchList(String id) {
		String path = utility.getJobDir() + "/" + id;
		File thisJobDir = new File(path);
		ArrayList<String> retval = new ArrayList<String>();
		try {
			for (String p : thisJobDir.list()) {
				FileReader fis = new FileReader(new File(path + "/" + p));
				BufferedReader br = new BufferedReader(fis);
				String sCurrentLine = "";

				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.trim();
					if (sCurrentLine.length() > 0) {
						retval.add(sCurrentLine);
					}
				}
				br.close();
			}
			
			return retval;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
