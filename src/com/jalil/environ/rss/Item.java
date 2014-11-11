package com.jalil.environ.rss;

import java.net.URL;

public class Item {
	
	private String title;
	private URL link;
	private String description;

	private Item() {}

	public Item(String title, URL link, String description) {
		this.title = title;
		this.link = link;
		this.description = description;
	}

	public String getTitle() {
	    return title;
    }

	public URL getLink() {
	    return link;
    }

	public String getDescription() {
	    return description;
    }
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof Item))
			return false;
		
		Item that = (Item) o;
		
		if (!title.equals(that.title))
			return false;
		if (!link.equals(that.link))
			return false;
		if(description != null ? !description.equals(that.description) : that.description != null)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = title.hashCode();
		result = 31 * result + link.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

	@Override
    public String toString() {
	    return "Item [title=" + title + ", link=" + link + ", description="
	            + description + "]";
    }
}
