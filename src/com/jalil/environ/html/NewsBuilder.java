package com.jalil.environ.html;


public class NewsBuilder {

	private String meta;
	private String body;

	public NewsBuilder() {}
	
	public NewsBuilder meta(String meta) {
		this.meta = meta;
		return this;
	}
	
	public NewsBuilder body(String body) {
		this.body = body;
		return this;
	}
	
	public News build() {
		return new News(meta, body);
	}
}
