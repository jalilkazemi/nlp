package com.jalil.environ.rss.builder;

import java.net.URL;

import com.jalil.environ.rss.Item;

public class ItemBuilder {

	private String title;
	private URL link;
	private String description;

	public ItemBuilder() {
	}
	
	public ItemBuilder title(String title) {
		this.title = title;
		return this;
	}
	
	public ItemBuilder link(URL link) {
		this.link = link;
		return this;
	}
	
	public ItemBuilder description(String description) {
		this.description = description;
		return this;
	}
		
	public Item build() {
		return new Item(title, link, description);
	}
}
