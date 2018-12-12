package TrashFiles;

public class MapLeaderServer {
	
	@SuppressWarnings("unused")
	void initiateMapper()
    { 
        int n = 8; // TODO: Number of threads from the config file
        for (int i=0; i<8; i++) 
        { 
            mapper object = new mapper(); 
            object.start(); 
        } 
    }

}

class mapper extends Thread{
	
	public void run() 
    { 
        try
        { 
            // Displaying the thread that is running 
            System.out.println ("Thread " + 
                  Thread.currentThread().getId() + 
                  " is running"); 
  
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("Exception is caught"); 
        } 
    } 
	
}