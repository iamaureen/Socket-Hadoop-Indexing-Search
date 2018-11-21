package LearningServers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	private Socket socket = null;
	private ObjectInputStream brinp = null;
	private ObjectOutputStream out = null;
	private int numEchoes = 0;

	public Client(String address, int port, int numE) {
		numEchoes = numE;
		// establish connection with server
		
		try {
			socket = new Socket(address, port);

			// create the streams
			
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			
			brinp = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	public boolean startCommunication() {
		// handle the reading part
		int val = 1;
		try {
			out.writeObject(val);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				val = (int) brinp.readObject() + 1 ;

				if ((val == -1) || val == this.numEchoes) {
					out.writeObject("quit");
					out.flush();
					socket.close();
					return true;
				} else {
					out.writeObject(val);
					out.flush();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public static void main(String[] args) {
		Client cl = new Client("127.0.0.1", 12345, 200);
		cl.startCommunication();
	}

}
