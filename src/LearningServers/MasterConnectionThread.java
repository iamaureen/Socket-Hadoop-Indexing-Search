package LearningServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MasterConnectionThread extends Thread {
	protected Socket socket;
	private ArrayList<String> WorkIDBuffer = new ArrayList<String>();
	private ArrayList<Object> outbox;

	public MasterConnectionThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {
		// create the object streams to read on
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

		// Here is the communication
		Object obj;
		while (true) {
			try {
				// parse input
				String line = "";
				obj = in.readObject();
				if (obj.getClass() == String.class) {
					line = (String) obj;
					// do something
					if ((line == null) || line.trim().equalsIgnoreCase("QUIT")) {
						System.out.println(line);
						socket.close();
						return;
					}
					if (line.equalsIgnoreCase("worker") || line.equalsIgnoreCase("client")) {
						Master.addConnection(this, line);

					}
				} else {
					this.giveWorkToMaster(obj);

				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				return;
			}

		}
	}

	private void giveWorkToMaster(Object obj) {
		String otherID = ((Work) obj).getId();
		
		if(!this.WorkIDBuffer.contains(otherID)) {
			Master.WorkQueue.add(obj);
			this.WorkIDBuffer.add(otherID);
			if(this.WorkIDBuffer.size()>15) {
				this.WorkIDBuffer.remove(0);
			}
		}

	}
}
