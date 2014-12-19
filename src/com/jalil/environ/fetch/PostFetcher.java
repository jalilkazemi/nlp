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
import com.jalil.environ.rss.Item;

public class PostFetcher {

	private Set<String> valuesOfAttributeClassInTagDivForBody; 
	private Set<String> valuesOfAttributeIdInTagDivForBody;
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
		valuesOfAttributeClassInTagDivForBody = Sets.newHashSet("entry-content", "body", "entry", "postcontent", 
				"full-text", "nwstxtmainpane", "BodyText");
		valuesOfAttributeIdInTagDivForBody = Sets.newHashSet("newsMainContent", "Body", "site_contents");
		valuesOfAttributeClassInTagDivForMeta = Sets.newHashSet("entry-meta", "publishDate", "title", "posttitle", 
				"news_nav news_pdate_c", "meta", "nwstxtdt", "NewsStatusBar", "newsPubDate");
	}
	
	public Post fetch(Item item) throws IOException {
		String addr = item.getLink();
		Document doc = Jsoup.parse(uriStreamer.stream(addr), null, addr);
		Elements divisions = doc.body().select("div");

		PostBuilder builder = new PostBuilder().item(item);
		boolean hasMeta = false, hasBody = false;
		for (int i = 0; i < divisions.size(); i++) {
			if (hasMeta && hasBody)
				break;
			Element division = divisions.get(i);
			if (valuesOfAttributeClassInTagDivForBody.contains(division.className())) {
				builder.body(division.text());
				hasBody = true;
			} else if (valuesOfAttributeIdInTagDivForBody.contains(division.id())) {
				builder.body(division.text());
				hasBody = true;
			} else if (valuesOfAttributeClassInTagDivForMeta.contains(division.className())) {
				builder.meta(division.text());
				hasMeta = true;
			}
		}

		System.out.println("PostFetcher: downloaded and parsed : " + addr);
		return builder.fetchedTime(new Date()).build();
	}
}
