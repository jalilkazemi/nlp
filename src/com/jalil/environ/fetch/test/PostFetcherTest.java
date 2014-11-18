package com.jalil.environ.fetch.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jalil.environ.fetch.UriStreamer;
import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.html.Post;

@RunWith(JUnit4.class)
public class PostFetcherTest {
	
	private static String postStr = "<html><head></head><body><div class=\"entry-meta\">meta</div><div class=\"entry-content\">body</div></body></html>";
	private static String postStrWithEntity = "<html><head><link title=\"a&raquo;b\"/></head><body></body></html>";
	
	@BeforeClass
	public static void init() throws IOException {
	}

	@Test
	public void testXmlParser() throws Exception {
		PostFetcher fetcher = new PostFetcher(new UriStreamer() {

			@Override
            public InputStream stream(String addr) throws MalformedURLException, IOException {
	            return new ByteArrayInputStream(postStr.getBytes(StandardCharsets.UTF_8));
            }
			
		});
		Post post = fetcher.fetch("");
		assertEquals("meta", post.getMeta());
		assertEquals("body", post.getBody());
	}

	@Test
	public void testXmlParserIgnoreEntity() throws Exception {
		PostFetcher fetcher = new PostFetcher(new UriStreamer() {

			@Override
            public InputStream stream(String addr) throws MalformedURLException, IOException {
	            return new ByteArrayInputStream(postStrWithEntity.getBytes(StandardCharsets.UTF_8));
            }
			
		});
		fetcher.fetch("");
	}
}
