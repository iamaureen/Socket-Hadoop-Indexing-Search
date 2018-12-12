package baseFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {

	public static int countLines(String filePath) throws IOException {

		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		byte[] byteArray = new byte[(int) file.length()];
		fis.read(byteArray);
		String data = new String(byteArray);
		String[] stringArray = data.split("\n");

		// System.out.println("Number of lines in the file are :: "+stringArray.length);
fis.close();
		return stringArray.length;
	}

//	public static int[] lineStartIndexes(String filePath) throws IOException {
//
//		RandomAccessFile raf = new RandomAccessFile(filePath, "r");
//
//		long position = raf.getFilePointer();
//		ArrayList<Long> linepositions = new ArrayList<Long>();
//		while (position < raf.length()) {
//			position = raf.getFilePointer();
//			linepositions.add(position);
//
//			raf.readLine();
//		}
//
//		int[] retval = new int[linepositions.size()];
//		for (int i = 0; i < retval.length; i++) {
//			retval[i] = (int) (long) linepositions.get(i);
//		}
//
//		return retval;
//	}

	/*
	 * public static String sendToWorker(String filename, int startPosition, int
	 * endPosition) throws IOException { // This filename must be the full canonical
	 * path for it to work RandomAccessFile raf = new RandomAccessFile(filename,
	 * "r"); int position = startPosition; byte[] readData = new byte[(int)
	 * raf.length()];
	 * 
	 * int length = endPosition - position; System.out.println(startPosition + "  "
	 * + endPosition); // System.out.println(length + " " + raf.length());
	 * raf.seek(position);
	 * 
	 * // System.out.println("i am here, where are you? here???"+ raf.read(readData,
	 * // position, length));
	 * 
	 * raf.read(readData, position, length); for (int i = 0; i < readData.length;
	 * i++) { System.out.println(readData[i]); } String sendToWorkerContent = new
	 * String(readData); // System.out.println("Did I read anything?\n" +
	 * sendToWorkerContent);
	 * 
	 * raf.close();
	 * 
	 * return sendToWorkerContent;
	 * 
	 * }
	 */
	public static String sendToWorker(String filename, int startPosition, int endPosition)
			throws FileNotFoundException {
		// This filename must be the full canonical path for it to work

		Scanner scan = new Scanner(new File(filename));

		scan.useDelimiter("\n");
		int count = 0;
		String retval = "";
		while (scan.hasNext()) {
			String val = scan.next();
			if (count >= startPosition && count <= endPosition) {
				retval += val + " \n";
			}

			count += 1;
		}
		scan.close();

		return retval;

	}
/*
	public static void main(String[] args) {
		String filePath = "sampleinput.txt";
		try {
			@SuppressWarnings("unused")
			File file = new File(filePath);

			// file length in bytes
			// System.out.println(file.length());

			// first step - count number of lines
			int numberOfLine = countLines(filePath);

			for (int i = 0; i < numberOfLine; i += 3) {
				System.out.println(sendToWorker(filePath, i, i + 2));
			}

			// System.out.println("something");
			// System.out.println(sendToWorker(filePath, 341, 677));

			// worker will tokenize the content
			// wordTokenizer.processContent(sendToWorker(filename, 341, 677));

			////
			// // create a new RandomAccessFile with filename test
			// RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
			// String line;
			//
			// int count = 3;
			// int i = 1;
			//
			// while ((line = raf.readLine()) != null) {
			//
			// int pointer = (int) (long) (raf.getFilePointer());
			// System.out.println("" + pointer);
			// raf.seek(raf.getFilePointer());
			// System.out.println(raf.readLine());
			//
			// i++;
			// if(i==3) {
			// System.out.println("byebye"+raf.getFilePointer());
			// break;
			// }
			//
			// }
			//

			// set the file pointer at 0 position
			// raf.seek(0);
			//
			// System.out.println("" + raf.getFilePointer());
			// String line = raf.readLine();
			// // print the line
			// System.out.println("" + raf.getFilePointer());
			// System.out.println("" + line);
			//
			// System.out.println("" + line.getBytes().length);
			// // set the file pointer at 0 position
			// raf.seek(line.getBytes().length+1);
			// // print the line
			// System.out.println("" + raf.readLine());
			//

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	*/

}
