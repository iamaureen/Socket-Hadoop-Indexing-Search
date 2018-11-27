package tinyGoogle;

import java.io.*;
import java.util.ArrayList;

public class DocumentIndexer {
	
	private ArrayList<DocumentIndex> DocIdList = new ArrayList<DocumentIndex>();
	
	DocumentIndexer(){
		parseFile();
	}
	
	void parseFile() {
		String workingDirectoty = System.getProperty("user.dir");
		String fileName = "documentList.txt";
		String filePath = workingDirectoty+"/src/tinyGoogle/" + fileName;
		
		System.out.println("Working Directory = " + filePath);
		
		BufferedReader br = null;
		FileReader fr = null;

		try {
			
			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			br.readLine(); //consume the first line and ignore

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				String[] sp = sCurrentLine.split(" ");
				
				//insert into the list
				DocumentIndex di = new DocumentIndex();
				di.setDocId(Integer.parseInt(sp[0]));
				di.setDocPath(sp[1]);
				
				DocIdList.add(di);
				
			}

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
	
	public boolean isDocumentPresent(int id) {
		
		for(int i = 0; i < DocIdList.size(); i++)
		{
		    System.out.println(DocIdList.get(i).getDocId());
		    if(DocIdList.get(i).getDocId() == id) {
		    		return true; //return DocIdList.get(i).getDocPath();
		    }
		}
		
		return false; //file id is not in the list
		
	}
	
	 public static void main(String[] args) {
		 //when DocumentIndexer is called it parses through the document and makes a list of existing documents
		 DocumentIndexer d = new DocumentIndexer();
		 
		 System.out.print(d.isDocumentPresent(1));
	       
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
