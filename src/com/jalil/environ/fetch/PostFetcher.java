package com.jalil.environ.fetch;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Sets;
import com.jalil.environ.html.Post;
import com.jalil.environ.html.PostBuilder;

public class PostFetcher {

	private Set<String> valuesOfAttributeClassInTagDivForBody; 
	private Set<String> valuesOfAttributeClassInTagDivForMeta; 
	private final UriStreamer uriStreamer;
	
	public PostFetcher() {
		this(new UriStreamerImpl());
	}

	public PostFetcher(UriStreamer uriStreamer) {
		this.uriStreamer = uriStreamer;
		setValidValuesForAttributeClassInTagDiv();
	}
	
	private void setValidValuesForAttributeClassInTagDiv() {
		valuesOfAttributeClassInTagDivForBody = Sets.newHashSet("entry-content", "body");
		valuesOfAttributeClassInTagDivForMeta = Sets.newHashSet("entry-meta", "publishDate");
	}
	
	public Post fetch(String addr) throws IOException {
		Document doc = Jsoup.parse(uriStreamer.stream(addr), null, addr);
		Elements divisions = doc.body().select("div");

		PostBuilder builder = new PostBuilder();
		boolean hasMeta = false, hasBody = false;
		for (int i = 0; i < divisions.size(); i++) {
			if (hasMeta && hasBody)
				break;
			Element division = divisions.get(i);
			if (valuesOfAttributeClassInTagDivForBody.contains(division.className())) {
				builder.body(division.text());
				hasBody = true;
			} else if (valuesOfAttributeClassInTagDivForMeta.contains(division.className())) {
				builder.meta(division.text());
				hasMeta = true;
			}
		}
		return builder.fetchedTime(new Date()).build();
	}
}
