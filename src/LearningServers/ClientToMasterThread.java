package LearningServers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import tinyGoogle.utility;

public class ClientToMasterThread extends Thread {

	protected Socket socket;

	// private ArrayList<String> JobIDBuffer = new ArrayList<String>();
	private Queue<Object> outbox;
	private Queue<Object> inbox;
	// private int jobBufferSize = 5;
	private ObjectOutputStream out;

	private volatile boolean goClose = true;

	public ClientToMasterThread() {
		this.outbox = new ConcurrentLinkedQueue<Object>();
		this.inbox = new ConcurrentLinkedQueue<Object>();
		socket = null;

		try {
			socket = new Socket(utility.MASTERHOST, utility.MASTERPORT);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		ObjectInputStream brinp = null;
		out = null;
		CTMInputStreamThread is = null;
		// create the streams
		//

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			brinp = new ObjectInputStream(socket.getInputStream());
			is = new CTMInputStreamThread(brinp, this);

			is.start();
			// tell the master server who I am
			System.out.println("sent machine name");
			out.writeObject(Client.clientName);
			out.flush();
		} catch (IOException e) {
			System.exit(0);
		}
	}

	public void run() {

		// Here is the communication loop
		Object obj;
		while (goClose) {
			try {
				Thread.sleep((int) Math.random() * 250);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				// parse input
				obj = this.inbox.poll();

				if (obj != null) {
					this.handleInput(obj);
				}

				// handle output
				// get front of queue
				Request send = (Request) this.outbox.poll();
				// type check and send
				if (send != null) {
					System.out.println("trying to send");
					out.writeObject(send);
					out.flush();
					System.out.println("sent");
					Client.startTime = System.nanoTime();
					System.out.println("The start time for this request is " + Client.startTime + "ns ");
					
					
				}

			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		}

	}

	private void handleInput(Object obj) {
		// this worker to master thread will only get jobs from master
		RequestAck j = (RequestAck) obj;
		boolean success = false;
		do {
			success = Client.WorkQueue.add(j.getStatus());
		} while (!success);

	}

	public boolean placeInOutbox(Object toSend) {
		try {
			Thread.sleep((int) Math.random() * 250);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			return this.outbox.add(toSend);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean placeInInbox(Object toReceive) {
		try {
			Thread.sleep((int) Math.random() * 250);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			return this.inbox.add(toReceive);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void close() {
		goClose = false;

	}

}

class CTMInputStreamThread extends Thread {
	private ObjectInputStream ois = null;
	private ClientToMasterThread hostThread = null;

	public CTMInputStreamThread(ObjectInputStream val, ClientToMasterThread host) {
		ois = val;
		hostThread = host;
	}

	public void run() {
		while (true) {

			try {
				boolean result = false;
				Object val = ois.readObject();
				do {
					result = hostThread.placeInInbox(val);
				} while (!result);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Client Closed");
				break;
				// e.printStackTrace();
			} 

		}
	}
}