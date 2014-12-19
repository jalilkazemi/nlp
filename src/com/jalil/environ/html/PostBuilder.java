package com.jalil.environ.html;

import java.util.Date;

import com.jalil.environ.rss.Item;


public class PostBuilder {

	private Item item;
	
	private String meta;
	private String body;
	private Date fetchedTime;

	public PostBuilder() {}
	
	public PostBuilder item(Item item) {
		this.item = item;
		return this;
	}

	public PostBuilder meta(String meta) {
		this.meta = meta;
		return this;
	}
	
	public PostBuilder body(String body) {
		this.body = body;
		return this;
	}
	
	public PostBuilder fetchedTime(Date fetchedTime) {
		this.fetchedTime = fetchedTime;
		return this;
	}
	
	public Post build() {
		return new Post(item, meta, body, fetchedTime);
	}
}
