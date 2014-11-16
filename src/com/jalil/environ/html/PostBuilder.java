package com.jalil.environ.html;


public class PostBuilder {

	private String meta;
	private String body;

	public PostBuilder() {}
	
	public PostBuilder meta(String meta) {
		this.meta = meta;
		return this;
	}
	
	public PostBuilder body(String body) {
		this.body = body;
		return this;
	}
	
	public Post build() {
		return new Post(meta, body);
	}
}
