package com.jalil.environ.fetch;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.jalil.environ.rss.RssFeed;

public class RssFetcher {
	
	private Unmarshaller jaxbUnmarshaller;
	
	public RssFetcher() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RssFeed.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}
	
	public RssFeed fetch(String addr) throws IOException, JAXBException {
		URL url = new URL(addr);
		return (RssFeed) jaxbUnmarshaller.unmarshal(url);
	}
}