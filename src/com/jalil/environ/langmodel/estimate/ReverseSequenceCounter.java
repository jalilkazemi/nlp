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

	public Set<T> lastItemSet() {
	    if (counter == null)
	    	return Collections.EMPTY_SET;
		return counter.keySet();
    }

	public Iterator<Entry<List<T>, Integer>> sequenceCountIterator() {
		if (counter == null) {
			return new Iterator<Entry<List<T>, Integer>>() {

				@Override
	            public boolean hasNext() {
		            return false;
	            }

				@Override
	            public Entry<List<T>, Integer> next() {
		            throw new NoSuchElementException();
	            }

				@Override
	            public void remove() {
					throw new UnsupportedOperationException();
	            }};			
		}
		
	    final Iterator<Entry<T, ReverseSequenceCounter<T>>> iter = counter.entrySet().iterator();
		return new Iterator<Entry<List<T>, Integer>>() {

			private Iterator<Entry<List<T>, Integer>> subIter = null;
			private T lastTermInSequence = null;
			
			@Override
            public boolean hasNext() {
	            return iter.hasNext() || (subIter != null && subIter.hasNext());
            }

			@Override
            public Entry<List<T>, Integer> next() {
				if (subIter == null || !subIter.hasNext()) {
					Entry<T, ReverseSequenceCounter<T>> item = iter.next();
					lastTermInSequence = item.getKey();
					subIter = item.getValue().sequenceCountIterator();
					return new Pair<List<T>, Integer>(Lists.newArrayList(lastTermInSequence), item.getValue().count());
				} else {
					Entry<List<T>, Integer> subItem = subIter.next();
					List<T> sequence = subItem.getKey();
					sequence.add(lastTermInSequence);
					return new Pair<List<T>, Integer>(sequence, subItem.getValue());
				}
            }

			@Override
            public void remove() {
				throw new UnsupportedOperationException();
            }};
    }
}
