package com.jalil.environ.fetch.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jalil.environ.fetch.AddrToReader;
import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.html.Post;

@RunWith(JUnit4.class)
public class PostFetcherTest {
	
	private static String postStr = "<html><head></head><body><div class=\"entry-meta\">meta</div><div class=\"entry-content\">body</div></body></html>";
	
	@BeforeClass
	public static void init() throws IOException {
	}

	@Test
	public void testXmlParser() throws IOException, JAXBException {
		PostFetcher fetcher = new PostFetcher(new AddrToReader() {

			@Override
            public Reader reader(String addr) throws MalformedURLException, IOException {
	            return new StringReader(postStr);
            }
			
		});
		Post post = fetcher.fetch("");
		assertEquals("meta", post.getMeta());
		assertEquals("body", post.getBody());
	}
}
