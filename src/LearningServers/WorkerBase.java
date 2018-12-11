package LearningServers;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import tinyGoogle.IIInterface;
import tinyGoogle.RandomAccessInputFile;
import tinyGoogle.WordCount;
import tinyGoogle.wordTokenizer;

public class WorkerBase {

	// This is a worker base and is supposed to template how a worker is supposed to
	// handle communication.

	// each worker will will have a server to communicate with the other servers

	// mapper will become server
	public static String workerName = "w-" + UUID.randomUUID().toString();
	public static LinkedBlockingQueue<Object> JobQueue = new LinkedBlockingQueue<Object>();

	public static void main(String[] args) {
		// first thing to do is connect to the master server
		// and we will have a thread for that
		new WorkerToMasterThread().start();

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

				if (ActiveJob.isIndexJob()) {
					// handle mapping task
					String mapTask = getTask(ActiveJob.getMapTasks());
					if (mapTask != null) {
						content = mapTask.split("|");
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
						for (String reduceTask : ActiveJob.getReduceTasks()) {
							content = reduceTask.split("|");
							WordCount toSave = wc.extract(content[1].charAt(0), content[2].charAt(0));
							JobSaver.saveWC(ActiveJob.getId(), content[1], content[2], workerName, toSave);
						}

					}
					// handle reducing task
					String reduceTask = getTask(ActiveJob.getReduceTasks());
					if (reduceTask != null) {
						content = reduceTask.split("|");

						// wait until all of the files have been created
						int numMappers = ActiveJob.getMapTasks().length;
						int count = 0;
						do {
							count = JobSaver.countWC(ActiveJob.getId(), content[1], content[2]);
						} while (count < numMappers);

						WordCount[] toMerge = JobSaver.collectWC(ActiveJob.getId(), content[1], content[2], count);

						// actual reduce step, merge everything together
						WordCount reducing = new WordCount();
						for (int i = 0; i < toMerge.length; i++) {
							reducing.merge(toMerge[i]);
						}
						
						//save each word into the II structure
						IIInterface.addEntry(term, DocId, count, path)

					}

				} else {

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private static String getTask(String[] assignments) {
		for (String s : assignments) {
			if (s.contains(workerName)) {
				return s;
			}
		}
		System.err.println("Worker " + workerName + " got a job it wasn't supposed to");
		return null;
	}

	public static WordCount countWords(String path, int start, int end) {
		try {
			String toParse = RandomAccessInputFile.sendToWorker(path, start, end);
			return wordTokenizer.processContent(toParse);

		} catch (IOException e) {
			System.err.println("Worker " + workerName + " gots an issue withs the IO");
		}
		return null;

	}

}
