package LearningServers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import tinyGoogle.utility;

public class WorkerToMasterThread extends Thread {

	protected Socket socket;

	private ArrayList<String> JobIDBuffer = new ArrayList<String>();
	// this outbox should only have job acks
	private Queue<Object> outbox;
	private Queue<Object> inbox;
	private int jobBufferSize = 5;

	public WorkerToMasterThread() {
		this.outbox = new ConcurrentLinkedQueue<Object>();
		this.inbox = new ConcurrentLinkedQueue<Object>();
		socket = null;

		try {
			socket = new Socket(utility.MASTERHOST, utility.MASTERPORT);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	public void run() {
		ObjectInputStream brinp = null;
		ObjectOutputStream out = null;
		WTMInputStreamThread is = null;
		// create the streams

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			brinp = new ObjectInputStream(socket.getInputStream());
			is = new WTMInputStreamThread(brinp, this);

			// tell the master server who I am
			out.writeObject(WorkerBase.workerName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				e.printStackTrace();
				return;
			}

		}

	}

	private void handleInput(Object obj) {
		// this worker to master thread will only get jobs from master
		Job j = (Job) obj;

		String otherID = j.getId();
		if (!this.JobIDBuffer.contains(otherID)) {
			boolean success = false;
			do {
				success = WorkerBase.JobQueue.add(obj);
			} while (!success);
			
			this.placeInOutbox(j.generateJobAck("job received", WorkerBase.workerName));
			
			this.JobIDBuffer.add(otherID);
			if (this.JobIDBuffer.size() > this.jobBufferSize) {
				this.JobIDBuffer.remove(0);
			}
		}
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

}

class WTMInputStreamThread {
	private ObjectInputStream ois = null;
	private WorkerToMasterThread hostThread = null;

	public WTMInputStreamThread(ObjectInputStream val, WorkerToMasterThread host) {
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