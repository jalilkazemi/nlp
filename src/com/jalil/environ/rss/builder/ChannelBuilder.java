package com.jalil.environ.rss.builder;

import java.net.URL;
import java.util.Set;

import com.jalil.environ.rss.Channel;
import com.jalil.environ.rss.Item;

public class ChannelBuilder {

	private String title;
	private String link;
	private String description;
	private String language;
	private Set<Item> items;

	public ChannelBuilder() {
	}
	
	public ChannelBuilder title(String title) {
		this.title = title;
		return this;
	}
	
	public ChannelBuilder link(String link) {
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

	public ChannelBuilder items(Set<Item> items) {
		this.items = items;
		return this;
	}

	public Channel build() {
		return new Channel(title, link, description, language, items);
	}
}
