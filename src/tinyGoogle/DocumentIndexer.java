package tinyGoogle;

import java.io.*;
import java.util.ArrayList;

public class DocumentIndexer {
	
	private ArrayList<DocumentIndex> DocIdList = new ArrayList<DocumentIndex>();
	int maxID; 
	
	DocumentIndexer(){
		parseFile();
	}
	
	void parseFile() {
		String filePath = getFilePath();
		
		BufferedReader br = null;
		FileReader fr = null;
		
		int max = 0;

		try {
			
			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			br.readLine(); //consume the first line and ignore

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				String[] sp = sCurrentLine.split(" ");
				
				//insert into the list
				DocumentIndex di = new DocumentIndex();
				di.setDocId(Integer.parseInt(sp[0]));
				di.setDocPath(sp[1]);
			
				DocIdList.add(di);
				
				//get the max ID
				if(Integer.parseInt(sp[0])>max) {
					max = Integer.parseInt(sp[0]);
				}
				
			}
			
			maxID = max;

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}	
	}
	
	public boolean isDocumentPresentByID(int id) {
		
		for(int i = 0; i < DocIdList.size(); i++)
		{
		    System.out.println(DocIdList.get(i).getDocId());
		    if(DocIdList.get(i).getDocId() == id) {
		    		return true; //return DocIdList.get(i).getDocPath();
		    }
		}
		
		return false; //file id is not in the list
		
	}
	
	public int isDocumentPresentByPath(String path) {
		
		int docID=0;
		for(int i = 0; i < DocIdList.size(); i++)
		{
		    System.out.println(DocIdList.get(i).getDocPath());
		    if(DocIdList.get(i).getDocPath().trim().equals(path)) {
		    		return DocIdList.get(i).getDocId();
		    } 
		}
		
		//enter a new entry (id, path) in the file and then return the ID
		
		//increase the maximum id present in the file by 1
		docID =  maxID+1;
		String toWriteInTheFile = "\n" + docID + " " + path;
		//append into the file with id and path
		appendStrToFile(getFilePath(), toWriteInTheFile);
		
		return docID;
		
		 //file id is not in the list
		
	}
	
	
	
	public void addFiletoDocumentIndex(String path) {
		
		
		
	}
	
	 public static void appendStrToFile(String fileName, 
             String str) 
	{ 
		try { 
				// Open given file in append mode. 
				BufferedWriter out = new BufferedWriter( new FileWriter(fileName, true)); 
				out.write(str); 
				out.close(); 
			} 
		catch (IOException e) { 
			System.out.println("exception occoured" + e); 	
		} 
	} 
	 
	 public String getFilePath() {
		 	String workingDirectoty = System.getProperty("user.dir");
			String fileName = "documentList.txt";
			String filePath = workingDirectoty+"/src/tinyGoogle/" + fileName;
			
			System.out.println("Working Directory = " + filePath);
			
			return filePath;
	 }
	
	 public static void main(String[] args) {
		 //when DocumentIndexer is called it parses through the document and makes a list of existing documents
		 DocumentIndexer d = new DocumentIndexer();
		 
		 System.out.print(d.isDocumentPresentByPath("arrypotter"));
	       
	  }

}

class DocumentIndex{
	
	private int DocId;
	private String DocPath;
	
	public int getDocId() {
		return DocId;
	}
	public void setDocId(int docId) {
		DocId = docId;
	}
	public String getDocPath() {
		return DocPath;
	}
	public void setDocPath(String docPath) {
		DocPath = docPath;
	}
	
}
