package com.jalil.environ.fetch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.common.collect.Sets;
import com.jalil.environ.html.Body;
import com.jalil.environ.html.Division;
import com.jalil.environ.html.Post;
import com.jalil.environ.html.PostBuilder;
import com.jalil.environ.rss.RssFeed;

public class PostFetcher {

	private Unmarshaller jaxbUnmarshaller;
	private Set<String> valuesOfAttributeClassInTagDivForBody; 
	private Set<String> valuesOfAttributeClassInTagDivForMeta; 

	public PostFetcher() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Body.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();		
		
		setValidValuesForAttributeClassInTagDiv();
	}
	
	private void setValidValuesForAttributeClassInTagDiv() {
		valuesOfAttributeClassInTagDivForBody = Sets.newHashSet("entry-content", "body");
		valuesOfAttributeClassInTagDivForMeta = Sets.newHashSet("entry-meta", "publishDate");
	}
	
	public Post fetch(String addr) throws MalformedURLException, JAXBException {
		URL url = new URL(addr);
		Body body = (Body) jaxbUnmarshaller.unmarshal(url);
		PostBuilder builder = new PostBuilder();
		boolean hasMeta = false, hasBody = false;
		for (Division div : body.getDivisions()) {
			if (hasMeta && hasBody)
				break;
			if (valuesOfAttributeClassInTagDivForBody.contains(div.getClassAttr())) {
				builder.body(div.getContent());
				hasBody = true;
			} else if (valuesOfAttributeClassInTagDivForMeta.contains(div.getClassAttr())) {
				builder.meta(div.getContent());
				hasMeta = true;
			}
		}
		return builder.build();
	}
}
