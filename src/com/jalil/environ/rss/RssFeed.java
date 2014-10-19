package com.jalil.environ.rss;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RssFeed {
	
	private final Channel channel;
	private final Set<Item> items;
	
	public RssFeed(Channel channel, Item... items) {
		this.channel = channel;
		this.items = new HashSet<Item>();
		if (items != null) {
			for (Item item : items) {
				this.items.add(item);			
			}
		}
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public Iterator<Item> getItems() {
		return items.iterator();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof RssFeed))
			return false;
		
		RssFeed that = (RssFeed) o;
		
		if (!channel.equals(that.channel))
			return false;
		if (!items.equals(that.items))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = channel.hashCode();
		result = 31 * result + items.hashCode();
		return result;
	}
}
