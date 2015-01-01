package com.jalil.environ.langmodel.estimate;

import com.jalil.environ.langmodel.Word;
import static com.jalil.environ.langmodel.Word.START;

public class TrigramMaxLikelihoodEstimate {

	private final int numSentences;
	private final ReverseSequenceCounter<Word> wordReverseSequenceCounter;

	public TrigramMaxLikelihoodEstimate(int numSentences, ReverseSequenceCounter<Word> wordReverseSequenceCounter) {
		this.numSentences = numSentences;
		this.wordReverseSequenceCounter = wordReverseSequenceCounter;
	}
	
	public double prob(Word u, Word v, Word w) {
		int marginal;
		if (u == START && v == START) {
			marginal = numSentences;
		} else {
			marginal = wordReverseSequenceCounter.count(u, v);
		}
		if (marginal == 0)
			return 0;
		
		int joint = wordReverseSequenceCounter.count(u, v, w);
		
		return (double) joint / marginal;
	}

	public double prob(Word v, Word w) {
		int marginal;
		if (v == START) {
			marginal = numSentences;
		} else {
			marginal = wordReverseSequenceCounter.count(v);
		}
		if (marginal == 0)
			return 0;
		
		int joint = wordReverseSequenceCounter.count(v, w);
		
		return (double) joint / marginal;
	}

	public double prob(Word w) {
		int marginal = wordReverseSequenceCounter.count();
		if (marginal == 0)
			return 0;
		
		int joint = wordReverseSequenceCounter.count(w);
		
		return (double) joint / marginal;
	}
	
	public int count(Word u, Word v) {
		return wordReverseSequenceCounter.count(u, v);
	}

	@Override
    public String toString() {
	    return "MarkovProbMaxLikelihoodEstimate [wordReverseSequenceCounter="
	            + wordReverseSequenceCounter + "]";
    }
}
