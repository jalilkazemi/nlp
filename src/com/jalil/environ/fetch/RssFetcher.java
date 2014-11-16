package com.jalil.environ.fetch;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.jalil.environ.rss.RssFeed;

public class RssFetcher {
	
	private final AddrToReader addrToReader;
	private Unmarshaller jaxbUnmarshaller;
	
	public RssFetcher() throws JAXBException {
		this(new AddrToReaderImpl());
	}
	
	public RssFetcher(AddrToReader addrToReader) throws JAXBException {
		this.addrToReader = addrToReader;

		JAXBContext jaxbContext = JAXBContext.newInstance(RssFeed.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}
	
	public RssFeed fetch(String addr) throws IOException, JAXBException {
		return (RssFeed) jaxbUnmarshaller.unmarshal(addrToReader.reader(addr));
	}
}