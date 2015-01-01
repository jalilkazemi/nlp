package com.jalil.environ.langmodel;

public class Word {
	
	public static Word STOP = new Word("_STOP_");
	public static Word START = new Word("_START_");
	
	private String word;
	
	private Word() {};
	
	private Word(String word) {
		if (word == null)
			throw new IllegalArgumentException("Word can't be null.");
		this.word = word;
	}
	
	public static Word newRegularWord(String word) {
		if (START.toString().equals(word))
			throw new IllegalArgumentException("The word matches the start token.");
		if (STOP.toString().equals(word))
			throw new IllegalArgumentException("The word matches the stop token.");

		return new Word(word);
	}

	public static Word newWord(String word) {
		if (START.toString().equals(word))
			return START;
		if (STOP.toString().equals(word))
			return STOP;
		
		return new Word(word);
	}
	
	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    Word other = (Word) obj;
	    if (word == null) {
		    if (other.word != null)
			    return false;
	    } else if (!word.equals(other.word))
		    return false;
	    return true;
    }
	
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + word.hashCode();
	    return result;
    }

	@Override
	public String toString() {
		return word;
	}
}
