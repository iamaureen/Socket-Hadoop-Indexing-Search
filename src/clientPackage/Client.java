package clientPackage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import baseFiles.Request;

public class Client {

	public static ConcurrentLinkedQueue<String> WorkQueue = new ConcurrentLinkedQueue<String>();
	public static String clientName = "c-" + UUID.randomUUID().toString();
	public static long startTime = 0;
	public static long endTime = 0;

	public static void main(String[] args) {
		// input check
		String term = null;

		if (args.length < 2) {
			System.out.println("Need two inputs a request type and the value as a valid path or search criteria");
			System.exit(1);
		}

		if (!(args[0].equals("index") || args[0].equals("search"))) {
			System.out.println("Sorry bad input please type 'index' or 'search'");
			System.exit(1);
		}

		if (args[0].equals("index")) {
			try {
				term = new File("./" + args[1]).getCanonicalPath();
			} catch (IOException e) {
				System.out.println("The file is not correct, please type it again");
				System.out.println(args[1]);
				System.exit(1);
			}
		}

		if (args[0].equals("search")) {
			term = args[1].toLowerCase();
		}

		if (term == null) {
			System.out.println("Something's fucky");
			System.exit(1);
		}

		System.out.println("Starting a(n) '" + args[0] + "' with criteria: " + term
				+ "\nPress CTRL-C in the next 5 seconds if this is incorrect");

		long seconds = System.nanoTime() / 1000000000;
		while (System.nanoTime() / 1000000000 < seconds + 5) {

		}

		String request = args[0];

		System.out.println("Building Request Package");
		Request r = new Request();
		r.setRequestType(request);
		r.setRequestVal(term);

		System.out.println("Creating connection");
		ClientToMasterThread CTM = new ClientToMasterThread();
		CTM.start();

		System.out.println("Sending message");
		boolean success = false;
		do {
			success = CTM.placeInOutbox(r);
		} while (!success);
		System.out.println("placed in outbox");
		String in = null;
		while (true) {
			try {
				Thread.sleep((int)Math.random()*250);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			in = WorkQueue.poll();
			if (in != null) {
				System.out.println(in);

				if (in.contains("DONE")) {
					System.out.println("Closing");
					CTM.close();
					endTime = System.nanoTime();
					System.out.println("The end time for this request is " + endTime + "ns ");
					
					System.out.println("The total time to handle this request is \n" + (endTime-startTime));
					
					System.exit(0);
				}

			}

		}

		// new ClientToMasterThread().start();

		// Scanner scan = new Scanner(System.in);

		// String input = "";

		// System.out.println("Hello, I am here to help you with this tiny google
		// implementation that we have, just follow the instructions I say.");

		// while(!input.equalsIgnoreCase("quit")) {
		//
		// System.out.println("What is your action? Please write 'index' or 'search'" );
		// String request = scan.nextLine().trim().toLowerCase();
		// System.out.println("");
		// if(request.equals("index")) {
		// boolean b = true;
		// while(b) {
		//
		// System.out.println("Please type the path of the file that you want to load
		// (relative) with a single slash");
		// String val = scan.nextLine().trim().toLowerCase();
		// File f = new File("./val");
		// val = f.getCanonicalPath();
		//
		// System.out.println();
		// System.out.println("Please check that the following is correct");
		// System.out.println("");
		// System.out.println("Request type: " + request);
		// System.out.println("Path: " + val);
		//
		// System.out.println("Is this correct? y/n");
		// String decision =
		//
		//
		// }
		//
		// }if(request.equals("search")) {
		// System.out.println("Please type your search terms");
		// String val = scan.nextLine().trim().toLowerCase();
		// } if(request.equalsIgnoreCase("quit")){
		// System.out.println("Shutting down.");
		// break;
		// }else {
		// System.out.println("I got bad input try again");
		// }
		//
		//
		//
		// }
	}

}
