package com.yippee.indexer;

import java.util.HashMap;

public class WordStemmer {
	HashMap<String,String> exceptionWords = new HashMap<String,String>();
	
	public WordStemmer(){
		exceptionWords.put("going", "go");
	}

	/**
	 * takes a word and implements stemming procedure
	 * 
	 * @param word
	 * @return stemmed word
	 */
	public String stem(String word) {
		int length = word.length();
		if (checkException(word)) {return exceptionWords.get(word);}
		else if(length>=3 && word.endsWith("ies")) {return word.substring(0,length-3)+"y";}
		else if(length>=5 && word.endsWith("ing")) {return word.substring(0,length-3);}
		else if(length>=3 && word.endsWith("ss")) {return word;}
		else if(length>=3 && word.endsWith("s")) {return word.substring(0,length-1);}
		else if(length>=3 && word.endsWith("ed")) {return word.substring(0,length-2);}
		else if(length>=3 && word.endsWith("ly")) {return word.substring(0,length-2);}
		else return word;
	}
	
	/**
	 * checks if this word is a common exception, and should remain the same
	 * 
	 * @param word
	 * @return
	 */
	public boolean checkException(String word) {
		if(exceptionWords.containsKey(word)) return true;
		else return false;
	}

	
	public static void main(String[] args) {
		WordStemmer w = new WordStemmer();
		String[] argss = {"friendly","jumping","caress","goes","camped","pastries","parts"};
		String[] argsss = {"operate", "operating", "operates", "operation", "operative", "operatives", "operational"};
		for(int i=0; i<argsss.length; i++){
			System.out.println(argsss[i]+" --> "+ w.stem(argsss[i]));
		}
	}
	
}
