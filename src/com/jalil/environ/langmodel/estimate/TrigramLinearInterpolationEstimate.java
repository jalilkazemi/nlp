package com.jalil.environ.langmodel.estimate;

import java.util.Arrays;

import static java.lang.Math.log;

import com.jalil.environ.langmodel.TrigramParser;
import com.jalil.environ.langmodel.Word;

public class TrigramLinearInterpolationEstimate {
	
	private final static int[] breaks = new int[] {0, 2, 5}; 
	private final static int numContingencies = breaks.length + 1;
	private final TrigramMaxLikelihoodEstimate markovML;
	private final double[] unigramCoeff, bigramCoeff, trigramCoeff;
	
	public TrigramLinearInterpolationEstimate(TrigramMaxLikelihoodEstimate markovML,
			double[] unigramCoeff, double[] bigramCoeff, double[] trigramCoeff) {
		if (unigramCoeff.length != numContingencies || 
				bigramCoeff.length != numContingencies || trigramCoeff.length != numContingencies) {
			throw new IllegalArgumentException("lambda's lengthes are invalid.");
		}
		this.markovML = markovML;
		this.unigramCoeff = Arrays.copyOf(unigramCoeff, unigramCoeff.length);
		this.bigramCoeff = Arrays.copyOf(bigramCoeff, bigramCoeff.length);
		this.trigramCoeff = Arrays.copyOf(trigramCoeff, trigramCoeff.length);
	}
	
	public TrigramLinearInterpolationEstimate refinement(double[] dUnigramCoeff, 
			double[] dBigramCoeff, double[] dTrigramCoeff) {
		TrigramLinearInterpolationEstimate newEstimate = 
				new TrigramLinearInterpolationEstimate(markovML, unigramCoeff, dBigramCoeff, trigramCoeff);
		for (int i = 0; i < numContingencies; i++) {
			newEstimate.unigramCoeff[i] += dUnigramCoeff[i];
			newEstimate.bigramCoeff[i] += dBigramCoeff[i];
			newEstimate.trigramCoeff[i] += dTrigramCoeff[i];	
		}
		return newEstimate;
	}
	
	public double logLikelihood(String sentence, String delimiters) {
		return new TrigramParser<Double>(sentence, delimiters) {

			private double logProb = 0.0;
			
			@Override
            public Double iteration(Word u, Word v, Word w) {
				logProb += termLL(u, v, w);
				return logProb;
            }}.parse();
	}
	
	private double termLL(Word u, Word v, Word w) {
		int count = markovML.count(u, v);
		int index = 0;
		for (int leftEnd : breaks) {
			if (count > leftEnd)
				index++;
		}
		
		return log(unigramCoeff[index] * markovML.prob(w) +
				bigramCoeff[index] * markovML.prob(v, w) +
				trigramCoeff[index] * markovML.prob(u, v, w));
	}

	@Override
    public String toString() {
	    return "TrigramLinearInterpolationEstimate [markovML=" + markovML
	            + ", unigram coefficient=" + Arrays.toString(unigramCoeff) + ", bigram coefficient="
	            + Arrays.toString(bigramCoeff) + ", trigram coefficient="
	            + Arrays.toString(trigramCoeff) + "]";
    }
}
