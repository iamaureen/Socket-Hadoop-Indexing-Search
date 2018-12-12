package TrashFiles;

import java.io.*;
import java.util.*;
import java.net.*;

public class tinyServer {
	
	 //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private Queue<RequestItem> requestQueue = new LinkedList<>(); ;
  
    // constructor with port 
    public tinyServer(int port) 
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
                    if(Integer.parseInt(line)==1) {
                    		System.out.println("Call Index Master");
                    		//create request item object and insert into queue
                    		RequestItem ri = new RequestItem();
                    		
                    		ri.setRequestType("index");
                    		ri.setItem("index"); //path 
                    		
                    		requestQueue.add(ri);
                    		
                    		System.out.println("Elements of queue-"+requestQueue);
                    		
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
        tinyServer server = new tinyServer(5000); 
    } 

}


class RequestItem{
	private String requestType;
	private String item; // path if index, query if search
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	@Override
	  public String toString() {
	    return getRequestType() + " "+ getItem();
	  }
		
}
