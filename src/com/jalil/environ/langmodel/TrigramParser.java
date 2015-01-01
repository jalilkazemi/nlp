package com.jalil.environ.langmodel;

import static com.jalil.environ.langmodel.Word.START;
import static com.jalil.environ.langmodel.Word.STOP;
import static com.jalil.environ.langmodel.Word.newRegularWord;

import java.util.StringTokenizer;

public abstract class TrigramParser<T> {

	private final String sentence;
	private final String delimiters;

	public TrigramParser(String sentence, String delimiters) {
		this.sentence = sentence;
		this.delimiters = delimiters;
	}
	
	public T parse() {
		Word u = START, v = START;
		StringTokenizer wordTokenizer = new StringTokenizer(sentence, delimiters);
		while (wordTokenizer.hasMoreTokens()) {
			Word w = newRegularWord(wordTokenizer.nextToken());
			iteration(u, v, w);
			u = v;
			v = w;
		}
		return iteration(u, v, STOP);
	}
	
	public abstract T iteration(Word u, Word v, Word w);
}
