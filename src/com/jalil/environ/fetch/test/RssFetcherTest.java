package com.jalil.environ.fetch.test;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jalil.environ.fetch.RssFetcher;
import com.jalil.environ.rss.RssFeed;

@RunWith(JUnit4.class)
public class RssFetcherTest {
	
	private static String rssAddr = "http://www.iew.ir/feed/rss";
	
	@BeforeClass
	public static void init() throws IOException {
	}

	@Test
	public void testXmlParser() throws IOException, JAXBException {
		RssFetcher fetcher = new RssFetcher();
		RssFeed feed = fetcher.fetch(rssAddr);
		System.out.println(feed);
	}
}
