package tinyGoogle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCount implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<String, Integer> wc = new HashMap<String, Integer>();

	public void incrementandAdd(String word){
		
		if(wc.get(word) != null) {
			wc.put(word, wc.get(word)+1);
		}else {
			wc.put(word, 1);
		}	
	}
	
	public void incrementandAddbycount(String word, int count) {
		if(wc.putIfAbsent(word, count) != null) {
			wc.put(word, wc.get(word)+count);
		}
	}
	
	public void merge(WordCount obj) {
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
	
	public WordCount extract(char startsWith, char endsWith) {
		
		Map<String, Integer> extracted_map = new HashMap<String, Integer>();
		
		for (char start = startsWith; start <= endsWith; start++) {
			//returns a list of WC objects for each word
			String pattern = String.valueOf(start);
			for (Map.Entry<String, Integer> entry : wc.entrySet()) {
				if(entry.getKey().startsWith(pattern)) {
					//System.out.println(entry.getKey() + ":" + entry.getValue().toString());
					extracted_map.put(entry.getKey(), entry.getValue());
				}		    
			}
		}
		
		WordCount extracted_WCobj = new WordCount();
		extracted_WCobj.wc = extracted_map;
		return extracted_WCobj;
			
	}
	
	public void printWordCount() {
		for (Map.Entry<String, Integer> entry : wc.entrySet()) {
		    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}
	}
	
	public List<WCPair> toList(){
		List<WCPair> retval = new ArrayList<WCPair>();
		
		for(String s: this.wc.keySet()) {
			WCPair temp = new WCPair(s, this.wc.get(s));
			retval.add(temp);
		}
		
		return retval;
	}
	
}
