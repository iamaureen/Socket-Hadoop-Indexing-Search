package tinyGoogle;

import java.io.*;
import tinyGoogle.utility;
import tinyGoogle.wordTokenizer;

public class RandomAccessInputFile {
	
	
	
	static int countLines (String filePath) throws IOException {
		
	    File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[(int)file.length()];
        fis.read(byteArray);
        String data = new String(byteArray);
        String[] stringArray = data.split("\n");
        
        System.out.println("Number of lines in the file are :: "+stringArray.length);
        
        return stringArray.length;
	}
	
	 static String sendToWorker(int worker, String filename, int startPosition, int endPosition) throws IOException{
		 String filePath = utility.getFilePath(filename);
		 RandomAccessFile raf = new RandomAccessFile(filePath, "r");
		 int position = startPosition;
		 byte[] readData = new byte[(int) raf.length()];
		
		 int length = endPosition - position;
		 System.out.println(length + " "+ raf.length());
		 raf.seek(position);
		 
		 //System.out.println("i am here, where are you? here???"+ raf.read(readData, position, length));
		 
		 raf.read(readData, position, length);
		 String sendToWorkerContent = new String(readData, "UTF-8");
		 //System.out.println("Did I read anything?\n" + sendToWorkerContent);
		 
		 raf.close();
		 
		 return sendToWorkerContent;
		 
	 }
	
	public static void main(String[] args) {
		String filename = "sampleinput.txt";
		String filePath = utility.getFilePath(filename);
        try {
            File file = new File(filePath);
            
            //file length in bytes
            //System.out.println(file.length());
            
            //first step - count number of lines
            int numberOfLine = countLines(filePath);
            
            //System.out.println(sendToWorker(1, filename, 0, 341));
            //System.out.println("something");
            System.out.println(sendToWorker(1, filename, 341, 677));
            
           //worker will tokenize the content
            wordTokenizer.processContent(sendToWorker(1, filename, 341, 677));
            
            
            
//            
            // create a new RandomAccessFile with filename test
//            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
//            String line;
//            
//            int count = 4;
//            int i = 0;
//            
//            while ((line = raf.readLine()) != null) {
//            		System.out.println("" + raf.getFilePointer());
//            	
//            }
//       
           
        

            // set the file pointer at 0 position
//            raf.seek(0);
//            
//            System.out.println("" + raf.getFilePointer());
//            String line = raf.readLine();
//            // print the line
//            System.out.println("" + raf.getFilePointer());
//            System.out.println("" + line);
//
//            System.out.println("" + line.getBytes().length);
//            // set the file pointer at 0 position
//            raf.seek(line.getBytes().length+1);
//            // print the line
//            System.out.println("" + raf.readLine());
//            
            
            
        } catch (Exception e) {
        }
    }


	
	

}
