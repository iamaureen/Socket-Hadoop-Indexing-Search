package tinyGoogle;

import java.io.*;
import java.util.*;
import java.net.*;

public class tinyTryServer {
	
	 //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private Queue<RequestItem> requestQueue = new LinkedList<>(); ;
  
    // constructor with port 
    public tinyTryServer(int port) 
    { 
        // starts server and waits for a connection 
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = server.accept(); 
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 
  
            String line = ""; 
  
            	
            // reads message from client until "Over" is sent 
            while (!line.equals("Over")) 
            { 
                try
                { 
                    line = in.readUTF(); 
                    System.out.println(line); 
                    
                    String input = "this is a string string";
                    convertStringToWordCountObj(input);
                                 
            			
                    if(Integer.parseInt(line)==1) {
                    		System.out.println("Call Index Master");
                    		//create request item object and insert into queue
                    		
                    		
//                    		RequestItem ri = new RequestItem();
//                    		
//                    		ri.setRequestType("index");
//                    		ri.setItem("index"); //path 
//                    		
//                    		requestQueue.add(ri);
//                    		
//                    		System.out.println("Elements of queue-"+requestQueue);
//                    		
//                    		DocumentIndexer di = new DocumentIndexer();
//                    		di.setDocId(1);
//                    		System.out.println("doc id :: "+di.getDocId());
                    		
                    }else if(Integer.parseInt(line)==2) {
	                		System.out.println("Call Query Master");
	                		//create request item object and insert into queue
	                		
	                		RequestItem ri = new RequestItem();
                    		
                    		ri.setRequestType("query");
                    		ri.setItem("query"); //path 
                    		
                    		requestQueue.add(ri);
                    		
                    		System.out.println("Elements of queue-"+requestQueue);
                		
                    }
  
                } 
                catch(IOException i) 
                { 
                    System.out.println(i); 
                } 
            } 
            System.out.println("Closing connection"); 
  
            // close connection 
            socket.close(); 
            in.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public static void main(String args[]) 
    { 
        tinyTryServer server = new tinyTryServer(5000); 
        
        
    } 
    
    static void convertStringToWordCountObj(String input) {
    	
    	String[] stringinput = input.split("\\s+"); // splits by whitespace
        
        WordCount wcObj = new WordCount();
        for (String word: stringinput) {
        		wcObj.incrementandAdd(word);
        }
        wcObj.printWordCount();
    		
    }

}



