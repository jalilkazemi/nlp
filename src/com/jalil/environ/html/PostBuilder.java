package com.jalil.environ.html;

import java.util.Date;


public class PostBuilder {

	private String meta;
	private String body;
	private Date fetchedTime;

	public PostBuilder() {}
	
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
		return new Post(meta, body, fetchedTime);
	}
}
