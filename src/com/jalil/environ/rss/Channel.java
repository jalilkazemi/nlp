package com.jalil.environ.rss;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Channel {
	
	private String title;
	private String link;
	private String description;
	private String language;
	private Set<Item> items;
	
	private Channel() {}

	public Channel(String title, String link, String description, String language, Item... items) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.items = new HashSet<Item>();
		if (items != null) {
			for (Item item : items)
				this.items.add(item);			
		}
	}

	public String getTitle() {
	    return title;
    }

	public String getLink() {
	    return link;
    }

	public String getDescription() {
	    return description;
    }

	public String getLanguage() {
	    return language;
    }
	
	public Iterator<Item> getItems() {
		return items.iterator();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof Channel))
			return false;
		
		Channel that = (Channel) o;
		
		if (!title.equals(that.title))
			return false;
		if (!link.equals(that.link))
			return false;
		if(description != null ? !description.equals(that.description) : that.description != null)
			return false;
		if(language != null ? !language.equals(that.language) : that.language != null)
			return false;
		if (!items.equals(that.items))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = title.hashCode();
		result = 31 * result + link.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (language != null ? language.hashCode() : 0);
		result = 31 * result + items.hashCode();
		return result;
	}
	
	@Override
    public String toString() {
	    return "Channel [title=" + title + ", link=" + link + ", description="
	            + description + ", language=" + language + ", items=" + items.toString() + "]";
    }
}
