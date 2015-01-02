package com.jalil.environ.langmodel.estimate.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jalil.environ.langmodel.estimate.ReverseSequenceCounter;


@RunWith(JUnit4.class)
public class ReverseSequenceCounterTest {

	@Test
	public void testSequenceCounting() {
		ReverseSequenceCounter<String> counter = new ReverseSequenceCounter<String>();
		
		assertEquals(0, counter.count());
		assertEquals(0, counter.count("a"));
		assertEquals(0, counter.count("a", "b"));
		
		counter.increment("a");
		counter.increment("a");
		assertEquals(2, counter.count());
		assertEquals(2, counter.count("a"));
		
		counter.increment("a", "b");
		counter.increment("b");
		counter.increment("c", "a", "b");
		counter.increment("d", "a", "b");
		assertEquals(4, counter.count("b"));
		assertEquals(3, counter.count("a", "b"));
		assertEquals(1, counter.count("c", "a", "b"));
		assertEquals(1, counter.count("d", "a", "b"));
		
		Set<String> lastItemSet = Sets.newHashSet("a", "b");
		for (String letter : counter.lastItemSet()) {
			assertEquals(true, lastItemSet.remove(letter));
		}
		assertEquals(0, lastItemSet.size());
		
		Map<List<String>, Integer> sequenceCount = new HashMap<List<String>, Integer>();
		sequenceCount.put(Lists.newArrayList("b"), 4);
		sequenceCount.put(Lists.newArrayList("a", "b"), 3);
		sequenceCount.put(Lists.newArrayList("d", "a", "b"), 1);
		sequenceCount.put(Lists.newArrayList("c", "a", "b"), 1);
		sequenceCount.put(Lists.newArrayList("a"), 2);
		Iterator<Entry<List<String>, Integer>> iter = counter.sequenceCountIterator();
		while (iter.hasNext()) {
			Entry<List<String>, Integer> item = iter.next();
			assertEquals(sequenceCount.get(item.getKey()), item.getValue());
			sequenceCount.remove(item.getKey());
		}
		assertEquals(0, sequenceCount.size());
	}
}
