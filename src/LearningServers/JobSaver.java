package LearningServers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tinyGoogle.WordCount;
import tinyGoogle.utility;

public class JobSaver {

	public static void saveWC(String id, String start, String end, String workerName, WordCount toSave) {
		String path = utility.getJobDir() + "/" + id;
		new File(path).mkdir();

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

	}

	public static int countWC(String id, String start, String end) {
		String path = utility.getJobDir() + "/" + id;
		File thisJobDir = new File(path);

		String checkString = start + "-" + end;

		int retval = 0;

		for (String p : thisJobDir.list()) {
			if (p.startsWith(checkString)) {
				retval += 1;
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

}
