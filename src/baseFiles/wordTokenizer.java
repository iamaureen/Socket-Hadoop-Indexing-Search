package baseFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class wordTokenizer {

	wordTokenizer() {

	}

	public static String readFile() {
		StringBuilder content = new StringBuilder();

		String filePath = utility.getFilePath("sampleinput.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				content.append(sCurrentLine).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content.toString();
	}

	public static Set<String> getStopWords() {

		Set<String> stopwords = new HashSet<>();
		// list of stopwords - https://gist.github.com/larsyencken/1440509
		String filePath = utility.getFilePath("stopwords.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				if (!sCurrentLine.startsWith("#")) {
					stopwords.add(sCurrentLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stopwords;

	}

	public static WordCount processContent(String content) {
		// read the content of the file
		// String content = readFile();

		// replace punctuation
		// https://www.geeksforgeeks.org/removing-punctuations-given-string/
		// removing digits and punctuation from string
		content = content.replaceAll("\\p{Punct}", " ").replaceAll("\\d", " ").replaceAll("\\n", " ")
				.replaceAll("\\s+", " ").toLowerCase();

		// get the stop words
		Set<String> stopwords = getStopWords();

		// remove stopwords from the content
		String[] contentArray = content.split(" ");
		StringBuilder withoutStopWords = new StringBuilder();
		for (String s : contentArray) {
			if (!stopwords.contains(s)) {
				withoutStopWords.append(s + ' ');
			}
		}
		// content without the stop words
		content = withoutStopWords.toString();
		// System.out.println(content);

		// create word count object
		WordCount wcObj = new WordCount();
		boolean printed25 = false;
		boolean printed50 = false;
		String[] tokenizedInputArray = content.split("\\s+");
		for (int i =0; i< tokenizedInputArray.length; i++) {
			wcObj.incrementandAdd(tokenizedInputArray[i]);
			if(tokenizedInputArray.length/(i+1) == 4 && !printed25) {
				System.out.println("25% of the mapping done");
				printed25 = true;
			}
			if(tokenizedInputArray.length/(i+1)== 2 && !printed50) {
				System.out.println("50% of the mapping done");
				printed50 = true;
			}
		}
		// wcObj.printWordCount();

		return wcObj;
	}

	/*
	 * public static void main(String[] args) throws FileNotFoundException { String
	 * content = readFile(); processContent(content); }
	 */

}
