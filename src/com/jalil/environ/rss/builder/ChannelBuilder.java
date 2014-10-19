package com.jalil.environ.rss.builder;

import java.net.URL;

import com.jalil.environ.rss.Channel;

public class ChannelBuilder {

	private String title;
	private URL link;
	private String description;
	private String language;

	public ChannelBuilder() {
	}
	
	public ChannelBuilder title(String title) {
		this.title = title;
		return this;
	}
	
	public ChannelBuilder link(URL link) {
		this.link = link;
		return this;
	}
	
	public ChannelBuilder description(String description) {
		this.description = description;
		return this;
	}
	
	public ChannelBuilder language(String language) {
		this.language = language;
		return this;
	}
	
	public Channel build() {
		return new Channel(title, link, description, language);
	}
}
