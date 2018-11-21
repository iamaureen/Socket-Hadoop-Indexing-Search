package tinyGoogle;

import java.util.*;

public class WordCount {
	Map<String, Integer> wc = new HashMap<String, Integer>();
	
	void incrementandAdd(String word){
		
		if(wc.get(word) != null) {
			wc.put(word, wc.get(word)+1);
		}else {
			wc.put(word, 1);
		}	
	}
	
	void merge(WordCount obj) {
		//takes all other object and merges to this object
		for (String s: obj.wc.keySet()) {
			//check if s is in this wc
			if(this.wc.putIfAbsent(s, obj.wc.get(s)) != null) {
				this.wc.put(s, this.wc.get(s)+obj.wc.get(s));
			}
		}
		
		
	}
	
	//add toString
	
	//add from String to convert to wc object
	
	void split() {
		//returns a list of WC objects for each word
	}
	
	void printWordCount() {
		for (Map.Entry<String, Integer> entry : wc.entrySet()) {
		    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}
	}
	
}
