package LearningServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EchoThread extends Thread {
	protected Socket socket;

    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }	
	
    public void run() {
        InputStream inp = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            inp = socket.getInputStream();
            in = new ObjectInputStream(inp);
        } catch (IOException e) {
            return;
        }
        Object obj;
        while (true) {
            try {
            	String line = "";
            	obj = in.readObject();
            	if(obj.getClass() == String.class) {
            		line = (String)obj + "";
            	}else{
            		line = (int)obj+"";
            	}
                
                System.out.println("raw input " + line);
                if ((line == null) || line.trim().equalsIgnoreCase("QUIT")) {
                	System.out.println(line);
                    socket.close();
                    return;
                } else {
                	System.out.println(line);
                    out.writeObject(obj);
                    out.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}	

