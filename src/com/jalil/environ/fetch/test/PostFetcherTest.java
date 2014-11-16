package com.jalil.environ.fetch.test;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.html.Post;

@RunWith(JUnit4.class)
public class PostFetcherTest {
	
	private static String rssAddr = "http://www.iew.ir/1393/08/24/30843";
	
	@BeforeClass
	public static void init() throws IOException {
	}

	@Test
	public void testXmlParser() throws IOException, JAXBException {
		PostFetcher fetcher = new PostFetcher();
		Post post = fetcher.fetch(rssAddr);
		System.out.println(post);
	}
}
