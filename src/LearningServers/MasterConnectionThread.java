package LearningServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MasterConnectionThread extends Thread {
	protected Socket socket;
	private ArrayList<String> WorkIDBuffer = new ArrayList<String>();
	private Queue<Object> outbox;
	private Queue<Object> inbox;
	private int workerBufferSize = 15;
	private String connectedName;
	private String connectedHost;
	private int connectedPort;

	public MasterConnectionThread(Socket clientSocket) {
		this.socket = clientSocket;
		this.outbox = new ConcurrentLinkedQueue<Object>();
		this.inbox = new ConcurrentLinkedQueue<Object>();
		this.connectedHost = Helper.getConnectedHostName(clientSocket);
		this.connectedPort = Helper.getConnectedPort(clientSocket);
	}

	public boolean placeInOutbox(Object toSend) {
		try {
			return this.outbox.add(toSend);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean placeInInbox(Object toReceive) {
		try {
			return this.inbox.add(toReceive);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getConnectedName() {
		return this.connectedName;
	}

	public String getConnectedHostName() {
		return Helper.getConnectedHostName(this.socket);
	}

	public void run() {
		// create the object streams to read on
		InputStream inp = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		MCTInputStreamThread ist = null;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			inp = socket.getInputStream();
			in = new ObjectInputStream(inp);
			ist = new MCTInputStreamThread(in, this);
			ist.start();
		} catch (IOException e) {
			return;
		}

		// Here is the communication loop
		Object obj;
		while (true) {
			try {
				// parse input
				obj = this.inbox.poll();

				if (obj != null) {
					this.handleInput(obj);
				}

				// handle output
				// get front of queue
				Object send = this.outbox.poll();
				// type check and send
				if (send != null) {
					out.writeObject(send);
				}

			} catch (IOException e) {
				Master.removeConnection(this, this.connectedName);
				e.printStackTrace();
				return;
			}

		}
	}

	// TODO might need to change this method if we change our inputs
	private void handleInput(Object obj) throws IOException {
		String line = "";
		if (obj.getClass() == String.class) {
			line = (String) obj;
			// do something
			if ((line == null) || line.trim().equalsIgnoreCase("QUIT")) {
				System.out.println(line);
				this.socket.close();
				return;
			}
			if (line.startsWith("w-") || line.equalsIgnoreCase("client")) {
				this.connectedName = line;
				Master.addConnection(this, line);

			}
		} else {
			this.giveWorkToMaster(obj);
		}

	}

	// TODO might need to change this method if we change our inputs
	private void giveWorkToMaster(Object obj) {
		Work wobj = ((Work) obj);
		String otherID = wobj.getId();
		// this is done so I know who to respond to once this work is done.
		wobj.setMct(this);

		if (!this.WorkIDBuffer.contains(otherID)) {
			boolean success = false;

			// check if this object is a JobAck or not
			if (obj.getClass() == JobAck.class) {
				JobAck jobj = (JobAck) obj;
				// send directly to the appropriate JobCoordinator thread
				JobCoordinator jct = Master.jobMap.get(jobj.getId());
				do {
					success = jct.placeInInbox(jobj);
				} while (!success);

			} else {

				do {
					success = Master.WorkQueue.add(obj);
				} while (!success);
			}
			// save id for later
			this.WorkIDBuffer.add(otherID);
			if (this.WorkIDBuffer.size() > this.workerBufferSize) {
				this.WorkIDBuffer.remove(0);
			}
		}

	}
}

class MCTInputStreamThread extends Thread {

	private ObjectInputStream ois = null;
	private MasterConnectionThread hostThread = null;

	public MCTInputStreamThread(ObjectInputStream val, MasterConnectionThread host) {
		ois = val;
		hostThread = host;
	}

	public void run() {
		try {
			boolean result = false;
			Object val = ois.readObject();
			do {
				result = hostThread.placeInInbox(val);
			} while (!result);

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

}
