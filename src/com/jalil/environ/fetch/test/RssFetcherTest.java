package com.jalil.environ.fetch.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.jalil.environ.fetch.UriStreamer;
import com.jalil.environ.fetch.RssFetcher;
import com.jalil.environ.rss.Channel;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RssFetcherTest {
	
	private final static String rssStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <rss xmlns:fake=\"a\" xmlns:fakeml=\"b\" xmlns:itunes=\"a\" xmlns:content=\"c\" xmlns:dc=\"a\" version=\"2.0\">" +
			"<channel><title>channeltitle</title><link>www.channel.org</link><description>channeldescription</description><language>en</language><lastBuildDate>Sun</lastBuildDate><image><title>Business</title><link>http</link></image>" +
			"<item><title>item1</title><description>item1description</description><pubDate>Fri,</pubDate><link>www.channel.org/item1</link></item>" +
			"<item><title>item2</title><description>item2description</description><pubDate>Fri</pubDate><link>www.channel.org/item2</link></item></channel></rss>";
	
	private final Item item1 = new Item("item1", "www.channel.org/item1", "item1description");
	private final Item item2 = new Item("item2", "www.channel.org/item2", "item2description");
	private final Channel expectedChannel = new Channel("channeltitle", "www.channel.org", "channeldescription", "en", Sets.newHashSet(item1, item2));

	@BeforeClass
	public static void init() throws IOException {
	}

	@Test
	public void testXmlParser() throws IOException, JAXBException {
		RssFetcher fetcher = new RssFetcher(new UriStreamer() {

			@Override
            public InputStream stream(String addr) throws MalformedURLException, IOException {
	            return new ByteArrayInputStream(rssStr.getBytes(StandardCharsets.UTF_8));
            }
			
		});
		RssFeed rss = fetcher.fetch("");
		Channel channel = rss.getChannel();
		fastChannelTest(expectedChannel, channel);

		assertEquals(expectedChannel.getTitle(), channel.getTitle());
		assertEquals(expectedChannel.getLink(), channel.getLink());
		assertEquals(expectedChannel.getDescription(), channel.getDescription());
		assertEquals(expectedChannel.getLanguage(), channel.getLanguage());
		
		Map<String, Item> titleToItem = new HashMap<String, Item>();
		titleToItem.put(item1.getTitle(), item1);
		titleToItem.put(item2.getTitle(), item2);
		for (Item item : channel.getItems()) {
			Item expectedItem = titleToItem.get(item.getTitle());
			assertNotNull(expectedItem);
			assertEquals(expectedItem.getTitle(), item.getTitle());
			assertEquals(expectedItem.getLink(), item.getLink());
			assertEquals(expectedItem.getDescription(), item.getDescription());
		}
	}
	
	private void fastChannelTest(Channel expectedChannel, Channel actualChannel) {
		assertEquals(expectedChannel, actualChannel);
	}
}
