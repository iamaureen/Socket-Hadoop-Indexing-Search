package LearningServers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import tinyGoogle.DocumentIndexer;
import tinyGoogle.IIInterface;
import tinyGoogle.FileHandler;
import tinyGoogle.WCPair;
import tinyGoogle.WordCount;
import tinyGoogle.utility;
import tinyGoogle.wordTokenizer;

public class WorkerBase {

	// This is a worker base and is supposed to template how a worker is supposed to
	// handle communication.

	public static String workerName = "w-" + UUID.randomUUID().toString();
	public static LinkedBlockingQueue<Object> JobQueue = new LinkedBlockingQueue<Object>();

	public static void main(String[] args) {
		
		// first thing to do is connect to the master server
		// and we will have a thread for that
		WorkerToMasterThread commThread = new WorkerToMasterThread();
		commThread.start();

		//instantiate the II structure. and job directory
		IIInterface.setupStructure(utility.getBasePath());
		utility.setJobDir(".");
		
		// we will now enter the work loop

		boolean retry = false;
		String[] content;
		Job ActiveJob = new Job("Not a Job", false);
		while (true) {

			try {
				ActiveJob = retry ? ActiveJob : (Job) JobQueue.take();
				if (retry) {
					// reset the retry flag
					retry = false;
				}
				if(ActiveJob.getTargetValue().equals("QUIT") && ActiveJob.isIndexJob()) {
					System.exit(0);
				}
				
				// indexing request
				if (ActiveJob.isIndexJob()) {

					// handle mapping task
					String mapTask = getTask(ActiveJob.getMapTasks());
					
					if (mapTask != null) {
						content = mapTask.split("\\|");
						
						int start = Integer.parseInt(content[1]);
						int end = Integer.parseInt(content[2]);

						WordCount wc = countWords(ActiveJob.getTargetValue(), start, end);
						if (wc == null) {
							// if the countWords function produced an error then I would have gotten null
							// and I
							// want to try again
							// potentially add a count to stop after 5 tries
							retry = true;
							continue;
						}

						// break apart and save
						//(This is where the real shuffling with the sockets would come into play but we handled shuffling through the afs space.
						for (String reduceTask : ActiveJob.getReduceTasks()) {
							content = reduceTask.split("\\|");
							WordCount toSave = wc.extract(content[1].charAt(0), content[2].charAt(0));
							JobSaver.saveWC(ActiveJob.getId(), content[1], content[2], workerName, toSave);
						}

					}
					// handle reducing task
					String reduceTask = getTask(ActiveJob.getReduceTasks());
					if (reduceTask != null) {
						content = reduceTask.split("\\|");

						// wait until all of the files have been created
						int numMappers = ActiveJob.getMapTasks().length;
						int count = 0;
						do {
							count = JobSaver.countWC(ActiveJob.getId(), content[1], content[2]);
						} while (count < numMappers);

						// collect all of the word count objects
						WordCount[] toMerge = JobSaver.collectWC(ActiveJob.getId(), content[1], content[2], count);

						// actual reduce step, merge everything together
						WordCount reducing = new WordCount();
						for (int i = 0; i < toMerge.length; i++) {
							reducing.merge(toMerge[i]);
						}

						// save each word into the II structure
						DocumentIndexer dind = new DocumentIndexer();
						int did = dind.isDocumentPresentByPath(ActiveJob.getTargetValue());
						List<WCPair> wordCounts = reducing.toList();
						for (WCPair wc : wordCounts) {
							IIInterface.addEntry(wc.word, did, wc.count, ActiveJob.getTargetValue());
						}

						// and we are done with indexing let's respond with a job well done.
						JobAck ja = ActiveJob.generateJobAck("SUCCESS: Completed", workerName);
						boolean success = false;
						do {
							// keep trying to place into outbox until it succeeds
							success = commThread.placeInOutbox(ja);
						} while (!success);

					}

				}
				// search request
				else {
					// handle mapping task
					String mapTask = getTask(ActiveJob.getMapTasks());
					if (mapTask != null) {
						content = mapTask.split("\\|");

						char start = content[1].charAt(0);
						char end = content[2].charAt(0);

						// get the subset of the terms that this worker is responsible for
						List<String> termList = IIInterface.getFileList(start, end);
						ArrayList<String> termsToSave = new ArrayList<String>();

						for (String termFile : termList) {
							// extract term
							String term = IIInterface.getTerm(termFile);
							if (ActiveJob.getTargetValue().contains(term)) {
								termsToSave.add(termFile);
							}

						}

						JobSaver.saveSearchList(ActiveJob.getId(), termsToSave, workerName);

					}
					// handle reducing task
					String reduceTask = getTask(ActiveJob.getReduceTasks());
					if (reduceTask != null) {
						// this is really a pointless content as we don't care about the other values
						content = reduceTask.split("\\|");
						// wait until all of the files have been created
						int numMappers = ActiveJob.getMapTasks().length;
						int count = 0;
						do {
							count = JobSaver.countSearchList(ActiveJob.getId());
						} while (count < numMappers);

						// read it in when it is ready
						ArrayList<String> terms = null;
						do {
							terms = JobSaver.collectSearchList(ActiveJob.getId());
						} while (terms == null);

						// check that I have terms to send
						if (terms.size() == 0) {
							// and we are done with indexing let's respond with a job well done.
							JobAck ja = ActiveJob.generateJobAck(
									"FAIL: We searched through " + IIInterface.getFileList('a', 'z').size()
											+ " documents and did not find tour search terms", workerName);
							boolean success = false;
							do {
								// keep trying to place into outbox until it succeeds
								success = commThread.placeInOutbox(ja);
							} while (!success);
						}

						// now that I have the list of candidate files I can rank and acknowledge that
						// this process is done.
						String response = rankAndRetrieve(terms);

						// send back the response
						JobAck ja = ActiveJob.generateJobAck("SUCCESS: " + response, workerName);
						boolean success = false;
						do {
							// keep trying to place into outbox until it succeeds
							success = commThread.placeInOutbox(ja);
						} while (!success);

					}

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// ranking and retrieving is a global sum
	// response is the content that will be sent to the user
	private static String rankAndRetrieve(ArrayList<String> terms) {
		WordCount docID = new WordCount();
		for (String s : terms) {
			String[] content = s.split("-");
			String tempID = content[1];
			int count = Integer.parseInt(content[2].substring(0, content[2].indexOf(".")));

			docID.incrementandAddbycount(tempID, count);
		}

		List<WCPair> lookthorugh = docID.toList();
		WCPair max = new WCPair("w", -1);
		for (WCPair w : lookthorugh) {
			if (w.count > max.count) {
				max = w;
			}
		}
		DocumentIndexer dind = new DocumentIndexer();
		String path = dind.isDocumentPresentByID(Integer.parseInt(max.word));

		String retval = "Document Found!\n\n" + "It is located here: " + path + "\n" + "With a toal Wordcount of: "
				+ max.count;

		return retval;
	}

	private static String getTask(String[] assignments) {
		for (String s : assignments) {
			if (s.contains(workerName)) {
				//System.out.println(s);
				return s;
			}
		}
		System.err.println("Worker " + workerName + " got a job it wasn't supposed to");
		return null;
	}

	public static WordCount countWords(String path, int start, int end) {
		try {
			String toParse = FileHandler.sendToWorker(path, start, end);
			return wordTokenizer.processContent(toParse);

		} catch (IOException e) {
			System.err.println("Worker " + workerName + " gots an issue withs the IO");
		}
		return null;

	}

}
