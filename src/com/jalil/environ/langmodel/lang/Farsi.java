package com.jalil.environ.langmodel.lang;

import java.util.LinkedList;
import java.util.List;

public class Farsi {

	private static List<Character> delimChars;
	private static List<Character> periodChars;
	
	static {
		int[] delimUnicodes = new int[] {32, 34, 40, 41, 171, 187, 1548};
		delimChars = new LinkedList<Character>();
		for (int unicode : delimUnicodes) {
			delimChars.add((char) unicode);
		}
		// TODO add carriage return as period
		int[] periodUnicodes = new int[] {10, 33, 46, 58, 1563, 1567};
		periodChars = new LinkedList<Character>();
		for (int unicode : periodUnicodes) {
			periodChars.add((char) unicode);
		}
	}
	
	public static List<Character> persianWordDelimiters() {
		return delimChars;
	}
	
	public static List<Character> persianSentenceDelimiters() {
		return periodChars;
	}
}
