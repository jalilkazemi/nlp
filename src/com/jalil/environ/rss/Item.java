package com.jalil.environ.rss;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "com.jalil.environ.rss.RssFeed")
public class Item {
	
	@XmlElement(name = "title")
	private String title;
	
	@XmlElement(name = "link")
	private String link;

	@XmlElement(name = "description")
	private String description;
	
	public Item() {}
	
	public Item(String title, String link, String description) {
		this.title = title;
		this.link = link;
		this.description = description;
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
