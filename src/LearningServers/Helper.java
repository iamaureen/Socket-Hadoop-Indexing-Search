package LearningServers;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Helper {
	//This class contains some server helper methods to use. 
	public static final int MASTERPORT = 12345;
	public static final String MASTERHOST = "127.0.0.1";//"oxygen.cs.pitt.edu";
	
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ex) {
			return "no host name";
		}
	}
	
	public static String getConnectedHostName(Socket sock) {
		return sock.getInetAddress().getHostAddress();
	}
	public static int getConnectedPort(Socket sock) {
		return sock.getPort();
	}
}
