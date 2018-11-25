package tinyGoogle;

import java.util.*;

public class WordCount implements java.io.Serializable {
	
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
	public String convertToString() {
		return (this.wc).toString();
	}
	
	WordCount extract(char startsWith, char endsWith) {
		
		Map<String, Integer> extracted_map = new HashMap<String, Integer>();
		
		for (char start = startsWith; start <= endsWith; start++) {
			//returns a list of WC objects for each word
			String pattern = String.valueOf(start);
			for (Map.Entry<String, Integer> entry : wc.entrySet()) {
				if(entry.getKey().startsWith(pattern)) {
					System.out.println(entry.getKey() + ":" + entry.getValue().toString());
					extracted_map.put(entry.getKey(), entry.getValue());
				}		    
			}
		}
		
		WordCount extracted_WCobj = new WordCount();
		extracted_WCobj.wc = extracted_map;
		return extracted_WCobj;
			
	}
	
	void printWordCount() {
		for (Map.Entry<String, Integer> entry : wc.entrySet()) {
		    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}
	}
	
}
