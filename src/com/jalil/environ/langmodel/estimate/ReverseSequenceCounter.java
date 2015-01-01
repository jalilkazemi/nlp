package com.jalil.environ.langmodel.estimate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.collect.Lists;
import com.jalil.environ.helper.Pair;

public class ReverseSequenceCounter<T> {

	private int totalCount = 0;
	private Map<T, ReverseSequenceCounter<T>> counter;
	
	public void increment(T... sequence) {
		if (sequence.length == 0)
			throw new IllegalArgumentException("Sequence can't have zero size.");
		increment(sequence, sequence.length - 1);
	}
	
	private void increment(T[] sequence, int from) {
		totalCount++;
		if (from < 0)
			return;
		if (counter == null) {
			counter = new HashMap<T, ReverseSequenceCounter<T>>();
		}
		ReverseSequenceCounter<T> subCounter = counter.get(sequence[from]);
		if (subCounter == null) {
			subCounter = new ReverseSequenceCounter<T>();
			counter.put(sequence[from], subCounter);
		}
		subCounter.increment(sequence, from - 1);
	}
	
	public int count(T... sequence) {
		return count(sequence, sequence.length - 1);
	}
	
	private int count(T[] sequence, int from) {
		if (from < 0)
			return totalCount;
		if (counter == null)
			return 0;
		
		ReverseSequenceCounter<T> subCounter = counter.get(sequence[from]);
		if (subCounter == null)
			return 0;
		return subCounter.count(sequence, from - 1);
	}

	@Override
    public String toString() {
	    return "ReverseSequenceCounter [totalCount=" + totalCount
	            + ", counter=" + counter + "]";
    }
}
