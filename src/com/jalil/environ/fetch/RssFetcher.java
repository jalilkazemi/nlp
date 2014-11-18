package com.jalil.environ.fetch;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.jalil.environ.rss.RssFeed;

public class RssFetcher {
	
	private final UriStreamer uriStreamer;
	private Unmarshaller jaxbUnmarshaller;
	
	public RssFetcher() throws JAXBException {
		this(new UriStreamerImpl());
	}
	
	public RssFetcher(UriStreamer uriStreamer) throws JAXBException {
		this.uriStreamer = uriStreamer;

		JAXBContext jaxbContext = JAXBContext.newInstance(RssFeed.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}
	
	public RssFeed fetch(String addr) throws IOException, JAXBException {
		RssFeed rss = (RssFeed) jaxbUnmarshaller.unmarshal(uriStreamer.stream(addr));
		System.out.println("RssFetcher: downloaded and parsed : " + 
							addr + " containing " + rss.getChannel().getItems().size() + 
							" items");
		return rss;
	}
}