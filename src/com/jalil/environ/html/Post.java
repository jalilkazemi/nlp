package com.jalil.environ.html;

import java.util.Date;

import com.jalil.environ.rss.Item;

public class Post {

	private Item item;

	private String meta;
	private String body;
	private Date fetchedTime;
	
	public Post(Item item, String meta, String body, Date fetchedTime) {
		this.item = item;
		this.meta = meta;
		this.body = body;
		this.fetchedTime = fetchedTime;
	}

	public Item getItem() {
		return item;
	}
	
	public String getMeta() {
    	return meta;
    }

	public String getBody() {
    	return body;
    }

	public Date getFetchedTime() {
		return fetchedTime;
	}
	
	@Override
    public String toString() {
	    return "Post [meta=" + meta + ", body=" + body + ", fetched at=" + fetchedTime + "]";
    }
}
