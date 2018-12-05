package tinyGoogle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;


public class wordTokenizer {
	
	
	
	wordTokenizer(){
		
	}
	
	public static String getFilePath(String filename) {
	 	String workingDirectoty = System.getProperty("user.dir");
		String fileName = filename;
		String filePath = workingDirectoty+"/src/tinyGoogle/" + fileName;
		
		System.out.println("Working Directory = " + filePath);
		
		return filePath;
	}
	
	public static String readFile() {
		StringBuilder content = new StringBuilder();
		
		String filePath = getFilePath("sampleinput.txt");
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
	
	public static Set<String> getStopWords() {
		
		Set<String> stopwords = new HashSet<>();
		//list of stopwords - https://gist.github.com/larsyencken/1440509
		String filePath = getFilePath("stopwords.txt");
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	        		//System.out.println(sCurrentLine);
	         	stopwords.add(sCurrentLine);
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
		
		return stopwords;
		
	}
	
	public static void processContent(String content) {
		//read the content of the file
		//String content = readFile();
		
		//replace punctuation
		//https://www.geeksforgeeks.org/removing-punctuations-given-string/
		content = content.replaceAll("\\p{Punct}","");
		
		//get the stop words
		Set<String> stopwords = new HashSet<>();
		stopwords = getStopWords();
		
		//remove stopwords from the content
		String[] contentArray = content.replace('\n', ' ').split(" ");
		StringBuilder withoutStopWords = new StringBuilder();
		for (String s : contentArray) {
		    if(!stopwords.contains(s)) {
		    		withoutStopWords.append(s+' ');
		    }
		}
		content = withoutStopWords.toString();
		System.out.println(content);
		
		
		//make a reader object to pass through the tokenizer
		Reader reader = new StringReader(content);
		
		//https://nlp.stanford.edu/software/tokenizer.shtml#Obtaining
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(reader,
	              new CoreLabelTokenFactory(), "");
	     
		  StringBuilder tokenizedInput = new StringBuilder();
	      while (ptbt.hasNext()) {
	        CoreLabel tokens = ptbt.next();
	        
	        System.out.println(tokens);
	        tokenizedInput.append(tokens.toString() + ' ');
	      }
	      
	      //create word count object
	      WordCount wcObj = new WordCount();
	      String[] tokenizedInputArray = tokenizedInput.toString().split("\\s+");
	        for (String word: tokenizedInputArray) {
	        		wcObj.incrementandAdd(word);
	        }
			wcObj.printWordCount();
			
			//return wcObj;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		String content = readFile();
		processContent(content);
	}
		
		

}
