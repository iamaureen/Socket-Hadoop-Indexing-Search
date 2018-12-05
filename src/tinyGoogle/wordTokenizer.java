package tinyGoogle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;


public class wordTokenizer {
	
	public static String getFilePath() {
	 	String workingDirectoty = System.getProperty("user.dir");
		String fileName = "sampleinput.txt";
		String filePath = workingDirectoty+"/src/tinyGoogle/" + fileName;
		
		System.out.println("Working Directory = " + filePath);
		
		return filePath;
	}
	
	public static String readFile() {
		StringBuilder content = new StringBuilder();
		
		String filePath = getFilePath();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	        		content.append(sCurrentLine).append("\n");
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return content.toString();
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		//read the content of the file
		String content = readFile();
		
		//replace punctuation
		content = content.replaceAll("\\p{Punct}","");
		
		//make a reader object to pass through the tokenizer
		Reader reader = new StringReader(content);
		
		//https://nlp.stanford.edu/software/tokenizer.shtml#Obtaining
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(reader,
	              new CoreLabelTokenFactory(), "");
	     
	      while (ptbt.hasNext()) {
	        CoreLabel label = ptbt.next();
	        
	        System.out.println(label);
	      }
	   }

}
